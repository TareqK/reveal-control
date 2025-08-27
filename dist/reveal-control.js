window.RevealControl = window.RevealControl || {
    id: 'reveal-control',
    init: (deck) => {
        initRevealControl(deck);
    }
};

const initRevealControl = function (deck) {
    const config = deck.getConfig().revealControl || {};
    const queryParams = new Proxy(new URLSearchParams(window.location.search), {
        get: (searchParams, prop) => searchParams.get(prop)
    });
    if(!!queryParams['print-pdf']){
        console.log('no control in pdf print mode')
        return;
    }
    if (queryParams.control === "true") {
        _revealControl_initControlMode(deck, config, queryParams);
    } else {
        _revealControl_initClientMode(deck, config, queryParams);
    }
};

const _revealControl_initControlMode = function (deck, config, queryParams) {
    const clientSessionId = queryParams.sessionId;
    deck.on('ready', () => {
        const revealControlSocket = new WebSocket(`${config.url}?sessionType=controller&sessionId=${clientSessionId}`);
        let slidesElement = deck.getSlidesElement();
        deck.on('slidechanged', (e) => {
            _revealControl_updateSlides(deck, revealControlSocket);
        });
        deck.on('fragmentshown', event => {
            _revealControl_updateSlides(deck, revealControlSocket);
        });
        deck.on('fragmenthidden', event => {
            _revealControl_updateSlides(deck, revealControlSocket);
        });
        const connectSlide = `
                                <section style="top: 20.5px; display: block;">
                                    <h2> Control Link Established. Proceed with Presenting</h2>
                                </section>
                            `;
        slidesElement.insertAdjacentHTML("afterbegin", connectSlide);
        deck.sync();
        deck.slide(0, 0, 0);
    });

};

const _revealControl_updateSlides = function (deck, revealControlSocket) {
    const state = deck.getState();
    setTimeout(() => {
        const update = {
            type: "updateSlides",
            "indexh": state.indexh,
            "indexv": state.indexv,
            "indexf": state.indexf
        };
        revealControlSocket.send(JSON.stringify(update));
    }, 15)//ensure that events dont get raced
}

const _revealControl_initClientMode = function (deck, config, queryParams) {
    deck.on('ready', () => {
        let slidesElement = deck.getSlidesElement();
        const revealControlSocket = new WebSocket(`${config.url}?sessionType=client&slideshowUrl=${encodeURIComponent(window.location)}`);
        revealControlSocket.onmessage = function (event) {
            let message = JSON.parse(event.data);
            switch (message.type) {
                case "clientSessionInfo":
                    const connectSlide = `
                                <section style="top: 20.5px; display: block;">
                                    <h2> Scan the below QR Code to take control </h2>
                                    <img src="data:image/png;base64, ${message.qrImage}"/>
                                    <br/>
                                    <a target="_blank" href="${message.url}">Or click here</a>
                                </section>
                            `;
                    slidesElement.insertAdjacentHTML("afterbegin", connectSlide);
                    deck.sync();
                    deck.slide(0, 0, 0);
                    break;
                case "updateSlides":
                    deck.slide(message.indexh, message.indexv, message.indexf);
                    break;
                default:
                    console.log(`Unknown Message Type : ${message.type}`);
                    break;
            }
        };
    });
};