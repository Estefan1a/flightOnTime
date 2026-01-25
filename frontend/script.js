
const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
console.log("Conectando al backend en:", API_BASE_URL);

// Estado
let currentTab = 'dashboard';
let currentPrediction = null;
let currentPage = 0;
const pageSize = 10;

// Inicialización
document.addEventListener('DOMContentLoaded', () => {
    setupTabs();
    setupForm();
    setupPagination();
    loadDashboard();
});

// Tabs
function setupTabs() {
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.addEventListener('click', () => {
            const tabName = tab.dataset.tab;
            switchTab(tabName);
        });
    });
}

function switchTab(tabName) {
    currentTab = tabName;
    
    document.querySelectorAll('.nav-tab').forEach(t => t.classList.remove('active'));
    document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');
    
    document.querySelectorAll('.content').forEach(c => c.classList.remove('active'));
    document.getElementById(`${tabName}-content`).classList.add('active');
    
    if (tabName === 'dashboard') {
        loadDashboard();
    } else if (tabName === 'history') {
        currentPage = 0;
        loadHistory();
    }
}

// Dashboard
async function loadDashboard() {
    await Promise.all([
        loadStats(),
        loadTopAirports()
    ]);
}

async function loadStats() {
    try {
        const response = await fetch(`${API_BASE_URL}/stats`);
        const data = await response.json();
        
        document.getElementById('total-predictions').textContent = data.totalPredicciones;
        document.getElementById('total-ontime').textContent = data.totalPuntuales;
        document.getElementById('total-delayed').textContent = data.totalRetrasados;
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

async function loadTopAirports() {
    const container = document.getElementById('airport-list');
    
    try {
        const response = await fetch(`${API_BASE_URL}/stats/airports/top`);
        const data = await response.json();
        
        if (data.length === 0) {
            container.innerHTML = '<p class="loading">No hay datos disponibles</p>';
            return;
        }
        
        container.innerHTML = data.map((airport, index) => `
            <div class="airport-item">
                <div class="airport-header">
                    <span class="airport-name">${airport.aeropuerto}</span>
                    <span class="airport-badge">#${index + 1}</span>
                </div>
                <div class="airport-stats">
                    <div class="airport-stat">
                        <div class="airport-stat-label">Total Vuelos</div>
                        <div class="airport-stat-value">${airport.totalPredicciones}</div>
                    </div>
                    <div class="airport-stat">
                        <div class="airport-stat-label">Retrasados</div>
                        <div class="airport-stat-value">${airport.totalRetrasados}</div>
                    </div>
                    <div class="airport-stat">
                        <div class="airport-stat-label">% Retrasos</div>
                        <div class="airport-stat-value">${(airport.porcentajeRetrasos * 100).toFixed(1)}%</div>
                    </div>
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error('Error loading airports:', error);
        container.innerHTML = '<p class="loading">Error al cargar aeropuertos</p>';
    }
}

// History
async function loadHistory() {
    const container = document.getElementById('history-list');
    container.innerHTML = '<div class="loading"><div class="loading-spinner"></div><p>Cargando historial...</p></div>';
    
    try {
        const response = await fetch(`${API_BASE_URL}/predict/history?page=${currentPage}&size=${pageSize}`);
        const data = await response.json();
        
        if (data.length === 0) {
            container.innerHTML = '<p class="loading">No hay predicciones registradas</p>';
            document.getElementById('pagination').style.display = 'none';
            return;
        }
        
        container.innerHTML = data.map(item => {
            const prevision = item.prevision.toLowerCase();
            const fechaPartida = new Date(item.fechaPartida).toLocaleString('es-ES', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
            const fechaPrediccion = new Date(item.fechaPrediccion).toLocaleString('es-ES', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
            
            return `
                <div class="history-item ${prevision}">
                    <div class="history-header">
                        <div class="history-route">
                            <div class="history-airline">${item.aerolinea}</div>
                            <div class="history-flight">
                                ${item.origen}
                                <span class="history-arrow">→</span>
                                ${item.destino}
                            </div>
                        </div>
                        <div class="history-badge">
                            ${prevision === 'puntual' ? '✓ Puntual' : '⚠ Retrasado'}
                        </div>
                    </div>
                    <div class="history-details">
                        <div class="history-detail">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                                <line x1="16" y1="2" x2="16" y2="6"></line>
                                <line x1="8" y1="2" x2="8" y2="6"></line>
                                <line x1="3" y1="10" x2="21" y2="10"></line>
                            </svg>
                            <div>
                                <div class="history-detail-label">Fecha de Partida</div>
                                <div class="history-detail-value">${fechaPartida}</div>
                            </div>
                        </div>
                        <div class="history-detail">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10"></circle>
                                <polyline points="12 6 12 12 16 14"></polyline>
                            </svg>
                            <div>
                                <div class="history-detail-label">Predicción Realizada</div>
                                <div class="history-detail-value">${fechaPrediccion}</div>
                            </div>
                        </div>
                        <div class="history-detail">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10"></circle>
                                <line x1="2" y1="12" x2="22" y2="12"></line>
                                <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"></path>
                            </svg>
                            <div>
                                <div class="history-detail-label">Confianza</div>
                                <div class="history-detail-value">${(item.probabilidad * 100).toFixed(1)}%</div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        }).join('');
        
        // Mostrar paginación si hay datos
        document.getElementById('pagination').style.display = 'flex';
        updatePaginationButtons(data.length);
        
    } catch (error) {
        console.error('Error loading history:', error);
        container.innerHTML = '<p class="loading">Error al cargar el historial</p>';
        document.getElementById('pagination').style.display = 'none';
    }
}

function setupPagination() {
    document.getElementById('refresh-history').addEventListener('click', () => {
        currentPage = 0;
        loadHistory();
    });
    
    document.getElementById('prev-page').addEventListener('click', () => {
        if (currentPage > 0) {
            currentPage--;
            loadHistory();
        }
    });
    
    document.getElementById('next-page').addEventListener('click', () => {
        currentPage++;
        loadHistory();
    });
}

function updatePaginationButtons(itemsCount) {
    const prevBtn = document.getElementById('prev-page');
    const nextBtn = document.getElementById('next-page');
    const pageInfo = document.getElementById('page-info');
    
    prevBtn.disabled = currentPage === 0;
    nextBtn.disabled = itemsCount < pageSize;
    pageInfo.textContent = `Página ${currentPage + 1}`;
}

// Form
function setupForm() {
    const form = document.getElementById('predict-form');
    form.addEventListener('submit', handleSubmit);
    
    document.querySelectorAll('input').forEach(input => {
        input.addEventListener('input', () => clearFieldError(input.name));
    });
}

async function handleSubmit(e) {
    e.preventDefault();
    
    const formData = {
        aerolinea: document.getElementById('aerolinea').value.trim().toUpperCase(),
        origen: document.getElementById('origen').value.trim().toUpperCase(),
        destino: document.getElementById('destino').value.trim().toUpperCase(),
        fechaPartida: document.getElementById('fechaPartida').value,
        distanciaKm: document.getElementById('distanciaKm').value
    };
    
    currentPrediction = { ...formData };
    
    const errors = validateForm(formData);
    if (Object.keys(errors).length > 0) {
        showErrors(errors);
        return;
    }
    
    clearErrors();
    await submitPrediction(formData);
}

function validateForm(data) {
    const errors = {};
    
    if (!data.aerolinea) {
        errors.aerolinea = 'La aerolínea es obligatoria';
    } else if (!/^[A-Za-z]{2}$/.test(data.aerolinea)) {
    errors.aerolinea = 'Debe ser un código de 2 letras (ej. AA, AM)';
}
    
    if (!data.origen) {
        errors.origen = 'El aeropuerto de origen es obligatorio';
    } else if (!/^[A-Z]{3}$/.test(data.origen)) {
    errors.origen = 'Debe ser un código IATA de 3 letras (ej. MEX)';
}
    
    if (!data.destino) {
        errors.destino = 'El aeropuerto de destino es obligatorio';
    } else if (!/^[A-Z]{3}$/.test(data.destino)) {
    errors.destino = 'Debe ser un código IATA de 3 letras (ej. JFK)';
}
    
    if (!data.fechaPartida) {
        errors.fechaPartida = 'La fecha de partida es obligatoria';
    } else {
        const selectedDate = new Date(data.fechaPartida);
        const now = new Date();
        if (selectedDate <= now) {
            errors.fechaPartida = 'La fecha de salida debe ser posterior a la fecha actual';
        }
    }
    
    if (!data.distanciaKm) {
        errors.distanciaKm = 'La distancia es obligatoria';
    } else if (parseInt(data.distanciaKm) <= 0 || parseInt(data.distanciaKm) > 20000) {
    errors.distanciaKm = 'La distancia debe ser válida';
}
    
    return errors;
}

function showErrors(errors) {
    clearErrors();
    Object.keys(errors).forEach(field => {
        const errorMsg = document.getElementById(`error-${field}`);
        const input = document.getElementById(field);
        
        if (errorMsg && input) {
            errorMsg.textContent = errors[field];
            errorMsg.classList.add('show');
            input.classList.add('error');
        }
    });
}

function clearErrors() {
    document.querySelectorAll('.error-msg').forEach(msg => {
        msg.classList.remove('show');
        msg.textContent = '';
    });
    document.querySelectorAll('input').forEach(input => {
        input.classList.remove('error');
    });
}

function clearFieldError(fieldName) {
    const errorMsg = document.getElementById(`error-${fieldName}`);
    const input = document.getElementById(fieldName);
    
    if (errorMsg) {
        errorMsg.classList.remove('show');
        errorMsg.textContent = '';
    }
    if (input) {
        input.classList.remove('error');
    }
}

async function submitPrediction(formData) {
    const submitBtn = document.getElementById('submit-btn');
    const btnText = document.getElementById('btn-text');
    const originalText = btnText.innerHTML;
    
    submitBtn.disabled = true;
    btnText.innerHTML = 'Prediciendo...';
    
    // Ocultar resultado anterior
    const resultDiv = document.getElementById('prediction-result');
    resultDiv.classList.remove('show');

    const payload = {
        ...formData,
        distanciaKm: parseInt(formData.distanciaKm)
    };

    //LOG 
    console.log("[FRONTEND] Enviando solicitud de predicción", payload);
    
    try {
        const response = await fetch(`${API_BASE_URL}/predict`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload)
            
        });

        console.log("[FRONTEND] Respuesta recibida del backend", response.status);
        
        // Error de validación (400)
        if (response.status === 400) {
            const errorData = await response.json();
            showErrors(errorData);
            showNotification('Por favor corrige los errores en el formulario', 'error');
            return;
        }
        
        // Error del servicio (503)
        if (response.status === 503) {
            const errorData = await response.json();
            showNotification(errorData.error || 'Error en el servicio de predicción', 'error');
            return;
        }
        
        // Otro error
        if (!response.ok) {
            const errorData = await response.json();
            showNotification(errorData.message || 'Error al realizar la predicción', 'error');
            return;
        }
        
        // Éxito
        const data = await response.json();
        showPredictionResult(data);
        
        // Limpiar formulario
        document.getElementById('predict-form').reset();
        
        // Actualizar dashboard
        loadStats();
        
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error de conexión con el servidor', 'error');
    } finally {
        submitBtn.disabled = false;
        btnText.innerHTML = originalText;
    }
}

function showPredictionResult(data) {
    const resultDiv = document.getElementById('prediction-result');
    const iconDiv = document.getElementById('prediction-icon');
    const statusDiv = document.getElementById('prediction-status');
    const descriptionDiv = document.getElementById('prediction-description');
    const probabilityDiv = document.getElementById('prediction-probability');
    
    const prevision = data.prevision.toLowerCase();
    
    // Remover clases anteriores
    resultDiv.classList.remove('puntual', 'retrasado');
    resultDiv.classList.add(prevision);
    
    // Iconos según el estado
    const icons = {
        puntual: '<polyline points="20 6 9 17 4 12"></polyline>',
        retrasado: '<circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline>'
    };
    
    iconDiv.innerHTML = icons[prevision] || icons.retrasado;
    
    // Estado
    const statusText = {
        puntual: '✓ Vuelo Puntual',
        retrasado: '⚠ Probable Retraso'
    };
    
    statusDiv.textContent = statusText[prevision] || statusText.retrasado;
    
    // Descripción explicativa
    const description = `El vuelo de la aerolínea ${currentPrediction.aerolinea} desde ${currentPrediction.origen} hacia ${currentPrediction.destino} tiene una probabilidad de ${prevision === 'puntual' ? 'salir a tiempo' : 'sufrir retrasos'}.`;
    
    descriptionDiv.textContent = description;
    
    // Mostrar información del clima
    if (data.weather) {
        document.getElementById('weather-temp').textContent = `${data.weather.temperatura.toFixed(1)}°C`;
        document.getElementById('weather-wind').textContent = `${data.weather.viento.toFixed(1)} km/h`;
        
        // Descripción del clima
        const weatherCondition = getWeatherCondition(data.weather.temperatura, data.weather.viento);
        document.getElementById('weather-description').textContent = weatherCondition;
    }
    
    // Probabilidad
    probabilityDiv.textContent = `Confianza: ${(data.probabilidad * 100).toFixed(2)}%`;
    
    // Mostrar resultado
    resultDiv.classList.add('show');
    
    // Scroll suave
    resultDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

function getWeatherCondition(temperatura, viento) {
    let tempDescription = '';
    let windDescription = '';
    
    if (temperatura < 0) {
        tempDescription = 'muy frías con temperaturas bajo cero';
    } else if (temperatura < 10) {
        tempDescription = 'frías';
    } else if (temperatura < 20) {
        tempDescription = 'templadas';
    } else if (temperatura < 30) {
        tempDescription = 'cálidas';
    } else {
        tempDescription = 'muy cálidas';
    }
    
    if (viento < 20) {
        windDescription = 'vientos suaves';
    } else if (viento < 40) {
        windDescription = 'vientos moderados';
    } else if (viento < 60) {
        windDescription = 'vientos fuertes';
    } else {
        windDescription = 'vientos muy fuertes';
    }
    
    return `Se esperan condiciones ${tempDescription} con ${windDescription} en el aeropuerto de destino.`;
}

function showNotification(message, type = 'error') {
    // Remover notificación anterior
    const existing = document.querySelector('.notification');
    if (existing) {
        existing.remove();
    }
    
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <svg class="notification-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"></circle>
                <line x1="12" y1="8" x2="12" y2="12"></line>
                <line x1="12" y1="16" x2="12.01" y2="16"></line>
            </svg>
            <span class="notification-text">${message}</span>
        </div>
        <button class="notification-close" onclick="this.parentElement.remove()">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"></line>
                <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
        </button>
    `;
    
    document.body.appendChild(notification);
    
    // Auto-cerrar después de 5 segundos
    setTimeout(() => {
        if (notification.parentElement) {
            notification.style.animation = 'slideOutRight 0.3s ease forwards';
            setTimeout(() => notification.remove(), 300);
        }
    }, 5000);
}