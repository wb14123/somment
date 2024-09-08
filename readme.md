

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

Currently there is no packaging and release yet. After build steps above, you need to copy the following files to use:

```
# the built Javascript file and source map
target/scala-3.5.0/somment-opt/somment.js
target/scala-3.5.0/somment-opt/somment.js.map

# for web component
demo/somment_component.js

# for css
demo/somment.css
```

It also depends on [DOMPurify](https://github.com/cure53/DOMPurify). So you should also include [purify.min.js](https://raw.githubusercontent.com/cure53/DOMPurify/3.1.6/dist/purify.min.js).


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
