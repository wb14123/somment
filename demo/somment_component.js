
import { Somment } from "./somment.js";


class SommentElement extends HTMLElement {

    static observedAttributes = ["link"];

    constructor() {
        super();
    }

    attributeChangedCallback(name, oldValue, newValue) {
        if (name !== "link") {
            return;
        }
        if (oldValue === newValue) {
            return;
        }
        const sommentElem = this.querySelector('.comments');
        if (sommentElem != null) {
            sommentElem.remove();
        }
        this.#createSommentElement();
    }

    #createSommentElement() {
        const elem = document.createElement("div");
        elem.setAttribute("id", "somment-root")
        this.appendChild(elem);

        const somment = new Somment(this.getAttribute("link"), elem);
        somment.create();
    }
}

customElements.define("somment-comment", SommentElement);
