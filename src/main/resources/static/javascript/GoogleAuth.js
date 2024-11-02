    <script>
        function handleCredentialResponse(response) {
            // Отправьте токен на сервер для проверки и дальнейшей обработки
            fetch('/oauth2/authorization/google', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ credential: response.credential }),
            })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/pages/main.html';
                } else {
                    alert('Błąd podczas logowania przez Google');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Wystąpił błąd po stronie klienta');
            });
        }

        window.onload = function () {
            google.accounts.id.initialize({
                client_id: '850220704064-8qv76pr9d2258pg6sn9n36n76tve3qcp.apps.googleusercontent.com',
                callback: handleCredentialResponse,
ux_mode: 'redirect'
            });
            google.accounts.id.renderButton(
                document.getElementById('g_id_signin'),
                { theme: 'outline', size: 'large' }  // Дополнительные параметры
            );
              google.accounts.id.prompt(); // Если вы хотите использовать One Tap
        };
gapi.auth2.getAuthInstance().signIn({ prompt: 'select_account', ux_mode: 'redirect' });

    </script>