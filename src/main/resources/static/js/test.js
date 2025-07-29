let events = [];
let currentPage = 0;
let eventsPerPage = 10;
let eventTypeFilter = "";

function connectEvents() {
    if (window.stompClient) return;
    let socket = new SockJS('/ws');
    window.stompClient = Stomp.over(socket);
    window.stompClient.connect({}, function () {
        for (let i = 1; i <= 10; i++) {
            window.stompClient.subscribe('/topic/events/player' + i, function (msg) {
                addEvent(JSON.parse(msg.body), 'player' + i);
            });
        }
        // 可根據需求訂閱聯盟
        // window.stompClient.subscribe('/topic/alliance/alliance1', ...);
    });
}

function addEvent(event, playerId) {
    event.playerId = playerId;
    events.unshift(event);
    renderEvents();
}

function renderEvents() {
    let tbody = document.getElementById('eventsTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';
    let filtered = events.filter(e => !eventTypeFilter || e.type === eventTypeFilter);
    let pageEvents = filtered.slice(currentPage * eventsPerPage, (currentPage + 1) * eventsPerPage);
    for (let e of pageEvents) {
        let tr = document.createElement('tr');
        tr.innerHTML = `<td>${new Date(e.timestamp).toLocaleString()}</td><td>${e.playerId}</td><td>${e.type}</td><td>${e.message}</td>`;
        tbody.appendChild(tr);
    }
}

function prevPage() {
    if (currentPage > 0) { currentPage--; renderEvents(); }
}
function nextPage() {
    let filtered = events.filter(e => !eventTypeFilter || e.type === eventTypeFilter);
    if ((currentPage + 1) * eventsPerPage < filtered.length) { currentPage++; renderEvents(); }
}
function clearEvents() {
    events = [];
    currentPage = 0;
    renderEvents();
}

window.onload = function () {
    let filter = document.getElementById('eventTypeFilter');
    if (filter) filter.onchange = function () {
        eventTypeFilter = filter.value;
        currentPage = 0;
        renderEvents();
    };
    let perPage = document.getElementById('eventsPerPage');
    if (perPage) perPage.onchange = function () {
        eventsPerPage = parseInt(perPage.value) || 10;
        currentPage = 0;
        renderEvents();
    };
};
