let xVal = null;
let rVal = null;
let results = [];

document.addEventListener("DOMContentLoaded", () => {
    const savedResults = localStorage.getItem('tblResults');
    if (savedResults) {
        results = JSON.parse(savedResults);
        reloadTable();
    }

    document.querySelectorAll('.x-button').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.x-button').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            xVal = parseInt(btn.value);
        });
    });

    document.querySelectorAll('.r-button').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.r-button').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            rVal = parseInt(btn.value);
        });
    });

    document.querySelector('.reset-button').addEventListener('click', () => {
        rVal = null;
        xVal = null;
        document.querySelectorAll('.x-button').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.r-button').forEach(b => b.classList.remove('active'));
        document.getElementById('y').value = '';
        document.getElementById('error').hidden = true;
    });

    function validateForm() {
        if (xVal === null) {
            document.getElementById('error').innerText = 'Выберите X';
            document.getElementById('error').hidden = false;
            return false;
        }

        const yStr = document.getElementById('y').value.trim().replace(',', '.');
        if (!/^-?\d+(\.\d+)?$/.test(yStr)) {
            document.getElementById('error').innerText =
                'Y должен быть числом';
            document.getElementById('error').hidden = false;
            return false;
        }
        const yVal = parseFloat(yStr);

        if (isNaN(yVal) || yVal < -3 || yVal > 3) {
            document.getElementById('error').innerText =
                'Y должен быть числом в диапазоне [-3; 3]';
            document.getElementById('error').hidden = false;
            return false;
        }

        if (rVal === null) {
            document.getElementById('error').innerText = 'Выберите R';
            document.getElementById('error').hidden = false;
            return false;
        }

        document.getElementById('error').hidden = true;
        return true;
    }

    document.getElementById('params').addEventListener('submit', async function (event) {
        event.preventDefault();
        if (!validateForm()) return;

        const yVal = document.getElementById('y').value.trim().replace(',', '.');
        const params = new URLSearchParams({x: xVal, y: yVal, r: rVal});

        try {
            const response = await fetch(`/fcgi-app?${params.toString()}`, {method: 'GET'});
            const data = await response.json();

            if (!response.ok) {
                const errorMsg = data.error || `Ошибка сервера: ${response.status}`;
                document.getElementById('error').innerText = errorMsg;
                document.getElementById('error').hidden = false;
                return;
            }

            const duration = parseFloat(data.execTime).toFixed(2);
            const currentTime = data.currentTime;
            const hit = data.hit;

            const newResult = {x: xVal, y: yVal, r: rVal, hit, duration, currentTime};
            results.push(newResult);

            saveResults();
            reloadTable();

        } catch (error) {
            const errorMsg = 'Нет соединения с сервером';
            document.getElementById('error').innerText = errorMsg;
            document.getElementById('error').hidden = false;
        }
    });

    function reloadTable() {
        const resultTable = document.getElementById('table-result');
        while (resultTable.rows.length > 1) {
            resultTable.deleteRow(1);
        }

        results.forEach(result => {
            addRowToTable(result);
        });
    }

    function addRowToTable(result) {
        const resultTable = document.getElementById('table-result');
        const newRow = resultTable.insertRow();
        newRow.innerHTML = `
            <td>${result.x}</td>
            <td>${result.y}</td>
            <td>${result.r}</td>
            <td>${result.duration} мс </td>
            <td>${result.currentTime}</td>
            <td>${result.hit ? 'Попадание' : 'Промах'}</td>
        `;
    }

    function saveResults() {
        localStorage.setItem('tblResults', JSON.stringify(results));
    }

    document.querySelector('.reset-button-table').addEventListener('click', () => {
        results = [];
        localStorage.removeItem('tblResults');
        reloadTable();
    });
});