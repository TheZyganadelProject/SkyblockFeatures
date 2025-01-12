package mrfast.sbf.gui.SideMenu;

import mrfast.sbf.mixins.transformers.GuiContainerAccessor;
import mrfast.sbf.utils.Utils;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;

import java.util.Arrays;

public abstract class CustomElement {
    protected int x, y, width, height, orginX, orginY;
    public static boolean beingClicked = false;
    public static CustomElement focusedElement;
    public boolean isFocused() {
        return this.equals(focusedElement);
    }
    private String hoverText;
    private Runnable onClickAction;
    SideMenu parent;


    // Constructor for button element
    public CustomElement(int x, int y, int width, int height, String hoverText, Runnable onClickAction) {
        this.x = x;
        this.y = y;
        this.orginX = x;
        this.orginY = y;
        this.width = width;
        this.height = height;
        this.hoverText = hoverText;
        this.onClickAction = onClickAction;
    }

    public void render(int x, int y) {

    }

    public void onKeyTyped(char character,int key) {

    }

    public void doHoverRender(int mouseX, int mouseY, GuiContainerAccessor gui) {
        this.x = (this.orginX + gui.getGuiLeft() + gui.getWidth() + parent.x);
        this.y = (this.orginY + gui.getGuiTop() + parent.y);
        // Check if the mouse is over the element
        boolean isHovered = isHovered(mouseX, mouseY);
        // Handle hover and click events
        if (isHovered) {
            onHover(mouseX, mouseY);
            if (Mouse.isButtonDown(0) && !beingClicked) {
                focusedElement = this;
                onClick(mouseX,mouseY,0);
            }
            beingClicked = Mouse.isButtonDown(0);
        } else {
            if(Mouse.isButtonDown(0)) {
                if(this instanceof TextInputElement) {
                    ((TextInputElement) this).textField.setFocused(false);
                }
            }
        }
    }

    private void onHover(int mouseX, int mouseY) {
        // Display hover text or perform additional hover logic
        if (hoverText != null) {
            GuiUtils.drawHoveringText(Arrays.asList(hoverText.split("\n")), mouseX, mouseY, Utils.GetMC().displayWidth, Utils.GetMC().displayHeight, -1, Utils.GetMC().fontRendererObj);
        }
    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        // Perform click action
        if (onClickAction != null) {
            onClickAction.run();
        }
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
