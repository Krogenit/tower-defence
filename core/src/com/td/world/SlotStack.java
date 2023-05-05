package com.td.world;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.td.entity.Tower;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class SlotStack extends Stack {
    private Slot slot;
    @Setter
    private Tower tower;

    public void setSlot(Slot slot) {
        this.slot = slot;
        add(slot);
    }

    public boolean isEmpty() {
        return tower == null;
    }
}
