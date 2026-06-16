const keyBoxes = document.getElementsByClassName("key");
const timeInputs = document.getElementsByClassName("time-input");

let clickedBox: Element | null = null;

for (const keyBox of keyBoxes) {
    keyBox.addEventListener("click", ()=>{
        setTimeout(()=>{
            clickedBox = keyBox;
            clickedBox.removeAttribute("key");
            clickedBox.removeAttribute("mouse");
            clickedBox.classList.add("selected-box");
        }, 10); //wait 10 ms to let the document click event process
    });
}

for (const timeInput of timeInputs) {
    timeInput.addEventListener("input", ()=>{
        timeInput.parentElement?.querySelector(".key")?.setAttribute("time",`${(timeInput as HTMLInputElement).value}`);
    });
}

document.addEventListener("keydown", (event)=> {
    if (clickedBox) {
        if (event.key==" ") {
            clickedBox.textContent = "Space";
            clickedBox.setAttribute("key", "space");
        }else {
            clickedBox.textContent = event.key;
            clickedBox.setAttribute("key", event.key);
        }
        
        clickedBox.classList.remove("selected-box");
        clickedBox = null;
    }
});

document.addEventListener("click", (event)=> {
    if (clickedBox) {
        if (event.button===0) {
            clickedBox.textContent = "Left Click";
            clickedBox.setAttribute("mouse","LEFT_MOUSE_DOWN");
        }else if (event.button===1) {
            clickedBox.textContent = "Middle Click";
            clickedBox.setAttribute("mouse","MIDDLE_MOUSE_DOWN");
        }else if (event.button===2) {
            clickedBox.textContent = "Right Click";
            clickedBox.setAttribute("mouse","RIGHT_MOUSE_DOWN");
        }
        
        clickedBox.classList.remove("selected-box");
        clickedBox = null;
    }
});

document.addEventListener("contextmenu", (event)=> { //detect right click
    if (clickedBox) {
        clickedBox.textContent = "Right Click";
        clickedBox.setAttribute("mouse","RIGHT_MOUSE_DOWN");
        
        clickedBox.classList.remove("selected-box");
        clickedBox = null;
    }
});





class Preset {
    private id: number;
    private name: string;

    constructor(id: number, name: string) {
        this.id = id;
        this.name = name;
    }

    getId() {
        return this.id;
    }

    getName() {
        return this.name;
    }
}

let presetDropdown: HTMLSelectElement = document.getElementById("preset-dropdown") as HTMLSelectElement;
let saveProfileButton = document.getElementById("save-profile-button");

let currentPreset: Preset | null = null;
currentPreset = new Preset(5,"lol");

saveProfileButton?.addEventListener("click",()=>{
    let promptName: string = prompt("What is the name of this preset?", presetDropdown.options[presetDropdown.selectedIndex]?.textContent) as string;
    const promptId = currentPreset!=null ? currentPreset.getId() : Math.random()*9999999;
    currentPreset = new Preset(promptId, promptName);
    fetch("/save-preset",{
        method: "POST",
        body: JSON.stringify({
            id: promptId,
            name: promptName
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    }).then(resp=>{
        if (resp.ok) return
    });
});

presetDropdown?.addEventListener("change",()=>{
    if (presetDropdown.value == "default") {
        currentPreset = null;
        return;
    }

    const selectedPreset = presetDropdown.options[presetDropdown.selectedIndex];
    currentPreset = new Preset(selectedPreset?.getAttribute("presetId"),selectedPreset?.textContent);
});