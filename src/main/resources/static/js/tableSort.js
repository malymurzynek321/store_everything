document.addEventListener('DOMContentLoaded', () => {
    const tables = document.querySelectorAll('table.sortable');

    tables.forEach(table => {
        const headers = table.querySelectorAll('th');

        headers.forEach((header, index) => {
            header.dataset.sortDirection = 'none';

            const icon = document.createElement('span');
            icon.className = 'sort-icon';
            header.appendChild(icon);

            header.addEventListener('click', () => {
                const sortDirection = header.dataset.sortDirection;
                let sortedRows;

                if (sortDirection === 'none') {
                    sortedRows = sortRows(index, 'asc');
                    header.dataset.sortDirection = 'asc';
                    updateIcons(headers, index, 'asc');
                } else if (sortDirection === 'asc') {
                    sortedRows = sortRows(index, 'desc');
                    header.dataset.sortDirection = 'desc';
                    updateIcons(headers, index, 'desc');
                } else if (sortDirection === 'desc') {
                    sortedRows = sortRows(0, 'asc');
                    headers.forEach((header, i) => {
                        header.dataset.sortDirection = i === 0 ? 'asc' : 'none';
                    });
                    updateIcons(headers, 0, 'asc');
                }

                updateTable(table, sortedRows);
            });
        });

        function sortRows(index, direction) {
            return Array.from(table.querySelector('tbody').rows)
                .sort((rowA, rowB) => {
                    const cellA = rowA.cells[index].innerText.toLowerCase();
                    const cellB = rowB.cells[index].innerText.toLowerCase();

                    if (direction === 'asc') {
                        return cellA.localeCompare(cellB);
                    } else {
                        return cellB.localeCompare(cellA);
                    }
                });
        }

        function updateTable(table, rows) {
            const tbody = table.querySelector('tbody');
            tbody.innerHTML = '';
            rows.forEach(row => tbody.appendChild(row));
        }

        function updateIcons(headers, activeIndex, direction) {
            headers.forEach((header, index) => {
                const icon = header.querySelector('.sort-icon');
                if (index === activeIndex) {
                    if (direction === 'asc') {
                        icon.innerHTML = ' ▲';
                    } else if (direction === 'desc') {
                        icon.innerHTML = ' ▼';
                    } else {
                        icon.innerHTML = '';
                    }
                } else {
                    icon.innerHTML = '';
                }
            });
        }
    });
});
