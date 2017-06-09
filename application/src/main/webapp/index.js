(function() {
    let config;
    let auth;
    let user;

    function loadApp() {
        return new Promise((resolve) => {
            gapi.load('auth2', resolve);
        });
    }

    function loadConfig() {
        return fetch('/api/login', method: 'POST', { credentials: 'include' })
            .then((response) => response.json());
    }

    function initApp(data) {
        config = data[1];
        auth = gapi.auth2.init({
           client_id: config.clientId,
           scope: 'profile'
        });

        auth.currentUser.listen(onUserChanged);

        if (auth.isSignedIn.get() === true) {
            auth.signIn();
        }

        refreshValues();

        const button = document.querySelector('[data-action="connect"]');
        button.addEventListener('click', onConnect);
    }

    function onUserChanged(data) {
        user = data;
        onUpdate();
    }

    function refreshValues() {
        if (auth) {
            user = auth.currentUser.get();
            onUpdate();
        }
    }

    function onConnect() {
        auth.grantOfflineAccess().then((response) => {
            connectToServer(response.code).then(() => {
                document.write(document.cookie);
            });
        });
    }

    function onUpdate() {
    }

    function connectToServer(code) {
        return fetch(`/api/login`, {
            credentials: 'include',
            method: 'POST',
            body: code
        });
    }

    Promise.all([
        loadApp(),
        loadConfig()
    ]).then(initApp)
})();