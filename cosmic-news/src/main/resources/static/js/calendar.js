let selectedEvent = null; // Definir a nivel global

document.addEventListener('DOMContentLoaded', () => {
    const calendarElement = document.getElementById('calendar');
    const modal = document.getElementById('dayModal');
    const modalDate = document.getElementById('modalDate');
    const modalInfo = document.getElementById('modalInfo');
    const closeModal = document.querySelector('.close');

    // Botones de acción en el modal
    const editButton = document.getElementById('editButton');
    const deleteButton = document.getElementById('deleteButton');
    const notifyButton = document.getElementById('notifyButton');

    let events = [];

    function fetchEvents() {
        fetch('/events') // Endpoint para obtener eventos
            .then(response => response.json())
            .then(data => {
                events = data;
                console.log(events);
                createCalendar(today.getFullYear(), today.getMonth());
            })
            .catch(error => console.error('Error fetching events:', error));
    }

    function createCalendar(year, month) {
        const date = new Date(year, month);
        const daysInMonth = new Date(year, month + 1, 0).getDate();
        const startDay = new Date(year, month, 1).getDay();

        const calendarHeader = `
            <div class="header">
                <button id="prevMonth">&lt;</button>
                <h3>${date.toLocaleString('default', { month: 'long' })} ${year}</h3>
                <button id="nextMonth">&gt;</button>
            </div>
        `;

        const daysHeader = `
            <div class="days">
                <div class="day day-header">Sun</div>
                <div class="day day-header">Mon</div>
                <div class="day day-header">Tue</div>
                <div class="day day-header">Wed</div>
                <div class="day day-header">Thu</div>
                <div class="day day-header">Fri</div>
                <div class="day day-header">Sat</div>
            </div>
        `;

        let days = '<div class="days">';
        for (let i = 0; i < startDay; i++) {
            days += '<div class="day"></div>';
        }
        for (let i = 1; i <= daysInMonth; i++) {
            const dateStr = `${year}-${(month + 1).toString().padStart(2, '0')}-${i.toString().padStart(2, '0')}`;
            const event = events.find(e => e.day === i && e.month === (month + 1) && e.year === year);
            const iconHtml = event ? `<i class="${event.icon} event-icon"></i>` : '';

            days += `<div class="day" data-day="${i}" data-date="${dateStr}" data-event-id="${event ? event.id : ''}">${i}${iconHtml}</div>`;
        }
        days += '</div>';

        calendarElement.innerHTML = calendarHeader + daysHeader + days;

        document.getElementById('prevMonth').addEventListener('click', () => {
            createCalendar(month === 0 ? year - 1 : year, month === 0 ? 11 : month - 1);
        });

        document.getElementById('nextMonth').addEventListener('click', () => {
            createCalendar(month === 11 ? year + 1 : year, month === 11 ? 0 : month + 1);
        });

        calendarElement.querySelectorAll('.day').forEach(day => {
            day.addEventListener('click', () => {
                if (day.dataset.day) {
                    calendarElement.querySelectorAll('.day').forEach(d => d.classList.remove('selected'));
                    day.classList.add('selected');
                    showDayInfo(day.dataset.day, month, year, day.dataset.eventId);
                }
            });
        });
    }

    function showDayInfo(day, month, year, eventId) {
        const formattedDate = `${year}-${(month + 1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
        selectedEvent = events.find(e => e.id === eventId); // Asignar el evento seleccionado
        const info = selectedEvent ? selectedEvent.description : 'No hay información adicional para este día.';
        modalDate.textContent = `Información para el ${day} de ${new Date(year, month).toLocaleString('default', { month: 'long' })} de ${year}`;
        modalInfo.textContent = info;
        modal.style.display = 'block';
    }

    closeModal.onclick = function() {
        modal.style.display = 'none';
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    }

    const today = new Date();
    fetchEvents(); // Fetch events and create calendar
});