document.getElementById('loginForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/api/auth/signin', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (!response.ok) throw new Error('Credenciales inválidas');
        
        const data = await response.json();
        localStorage.setItem('jwtToken', data.token);
        window.location.href = '/';
    } catch (error) {
        alert(error.message);
    }
});

// Middleware para verificar autenticación
const checkAuth = () => {
    const token = localStorage.getItem('jwtToken');
    if (!token && !window.location.pathname.includes('/auth')) {
        window.location.href = '/auth/login';
    }
};

checkAuth();