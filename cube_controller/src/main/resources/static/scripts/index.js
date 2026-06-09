const keyBoxes = document.getElementsByClassName("key");
const timeInputs = document.getElementsByClassName("time-input");
let clickedBox = null;
for (const keyBox of keyBoxes) {
    keyBox.addEventListener("click", () => {
        setTimeout(() => {
            clickedBox = keyBox;
            clickedBox.removeAttribute("key");
            clickedBox.removeAttribute("mouse");
            clickedBox.classList.add("selected-box");
        }, 10); //wait 10 ms to let the document click event process
    });
}
for (const timeInput of timeInputs) {
    timeInput.addEventListener("input", () => {
        timeInput.parentElement?.querySelector(".key")?.setAttribute("time", `${timeInput.value}`);
    });
}
document.addEventListener("keydown", (event) => {
    if (clickedBox) {
        if (event.key == " ") {
            clickedBox.textContent = "Space";
            clickedBox.setAttribute("key", "space");
        }
        else {
            clickedBox.textContent = event.key;
            clickedBox.setAttribute("key", event.key);
        }
        clickedBox.classList.remove("selected-box");
        clickedBox = null;
    }
});
document.addEventListener("click", (event) => {
    if (clickedBox) {
        if (event.button === 0) {
            clickedBox.textContent = "Left Click";
            clickedBox.setAttribute("mouse", "LEFT_MOUSE_DOWN");
        }
        else if (event.button === 1) {
            clickedBox.textContent = "Middle Click";
            clickedBox.setAttribute("mouse", "MIDDLE_MOUSE_DOWN");
        }
        else if (event.button === 2) {
            clickedBox.textContent = "Right Click";
            clickedBox.setAttribute("mouse", "RIGHT_MOUSE_DOWN");
        }
        clickedBox.classList.remove("selected-box");
        clickedBox = null;
    }
});
document.addEventListener("contextmenu", (event) => {
    if (clickedBox) {
        clickedBox.textContent = "Right Click";
        clickedBox.setAttribute("mouse", "RIGHT_MOUSE_DOWN");
        clickedBox.classList.remove("selected-box");
        clickedBox = null;
    }
});
export {};
//# sourceMappingURL=index.js.map