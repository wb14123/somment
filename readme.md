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
