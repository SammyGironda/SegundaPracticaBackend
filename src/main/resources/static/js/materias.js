const loadMaterias = async () => {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch('/api/materias', {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    const materias = await response.json();
    
    const container = document.getElementById('materiasList');
    container.innerHTML = materias.map(m => `
        <div class="card">
            <h3>${m.nombreMateria}</h3>
            <p>Código: ${m.codigoUnico}</p>
        </div>
    `).join('');
};