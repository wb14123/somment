

Somment is a Javascript library to show comments from different websites. You only need to provide a link to the page. Currently it supports:

* HackerNews
* Reddit

It's still in active development. There is no packaing. The APIs can change at any time.

## Build

Build and run demo:

```bash
sbt fastLinkJS // generate JS files
sbt copyToDemo // copy JS files to demo dir

cd demo
npm install
npm run dev // run the demo
```

Compile optimized JS:

```
sbt fullLinkJS
```

## Release Files

```
sbt npmRelease
```

If the auto run of `npm publish` in the command above failed because of 2FA issues, a workaround is to go to demo directory and run `npm publish` manually.

It also depends on [DOMPurify](https://github.com/cure53/DOMPurify). So you should also include [purify.min.js](https://raw.githubusercontent.com/cure53/DOMPurify/3.1.6/dist/purify.min.js). TODO: include this in npm dependencies.


## Usage

Include css and javascript file (`somment.js` should be put at the same level of `somment_component.js`), also `DOMPurify` dependency:

```html
<link rel="stylesheet" href="somment.css" />
<script type="text/javascript" src="lib/purify.min.js"></script>
<script type="module" src="/somment_component.js"></script>
```

Then use `somment-comment` tag with `link` attribute for the page you want to show comments for:

```html
<somment-comment link="https://news.ycombinator.com/item?id=41412256"></somment-comment>
```
