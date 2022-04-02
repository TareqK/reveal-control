# RevealControl

RevealControl is a plugin for reveal.js that allows you to control your 
reveal.js slideshows from a remote device. It works by mirroring 
the current slide position on the controller device on the client device.

## But Why?

Have you ever wanted to Tony Stark it on a stage with a tablet controlling your super 1337 slideshow? Have you ever wanted to 
absolutely flex those techlad muscles infront of management, clients, or stakeholders? Have you ever wanted to feel like an
absolute hype-beast on stage?

If the answer to any of these is yes, then I assume you understand why I made this.

## How does It Work?

![How It Works Diagram](http://www.plantuml.com/plantuml/proxy?cache=yes&src=https://raw.githubusercontent.com/TareqK/reveal-control/master/docs/how-it-works.iuml)


Effectively, We are setting up a connection to  mirror all reveal.js actions done on the controller presentation on the client presentation. this
allows us to control the client presentation without installing anything on the device or using a clicker.
 
## Usage

### Deploying a backend instance

The backend is built using [javalin](https://javalin.io) and [docker](https://docker.io). To deploy an instance, simply run

```bash
docker run -p 8080:8080 -d revealcontrol/backend:latest 

```

the server runs on port 8080 inside docker, you can bind it to any other suitable port. Its also a good idea to use
something like [nginx](https://nginx.com) for SSL termination.

There is also a public demo instance at [reveal-control.kisoft.me](https://reveal-control.kisoft.me), however, it is highly
inadvisable to use it in production. 

you can find specific versions of this image on [docker hub](https://hub.docker.com/repository/docker/revealcontrol/backend)

### Using the reveal.js plugin

to use the reveal.js plugin, you need to either copy over the reveal-control.js file in the ``dist`` folder of the [repository](https://github.com/TareqK/reveal-control),
or import it using [jsdelivr](https://jsdelivr.net) like so


```html
<script src="https://cdn.jsdelivr.net/gh/TareqK/reveal-control@master/dist/reveal-control.min.js"></script>
```

you can use specific images by changing the ``@master`` to ``@semver.release.version``. You can find the releases page [on Github](https://github.com/TareqK/reveal-control/releases)

This will fetch the reveal.js plugin. Afterwards, you simply need to configure the plugin during your reveal.js init like so

```js
Reveal.initialize({
            controls: true,
            progress: true,
            history: true,
            center: true,
            touch: true,
            transition: 'slide',
            hash: true,
            revealControl: {
                url: "ws://<your-server-and-port>/reveal-control"
            },
            plugins: [RevealHighlight, RevealMath.KaTeX, RevealNotes, RevealZoom, RevealControl]
        });
```

the ``revealControl`` config key is where all the relevant configuration is. For now, this is only the ``url`` property. When you open up a slideshow,
a QR code/Connection slide is injected in the first slide. Simply scan the code or use the link below it to take control of the slideshow.
