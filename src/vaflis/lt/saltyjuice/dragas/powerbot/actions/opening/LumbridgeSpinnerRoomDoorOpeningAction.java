package vaflis.lt.saltyjuice.dragas.powerbot.actions.opening;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;

public class LumbridgeSpinnerRoomDoorOpeningAction extends OpeningAction
{
    @Override
    protected int getClosedDoorId()
    {
        return Constant.Objects.Door.LUMBERIDGE_SPINNER_ROOM_DOOR_CLOSED;
    }

    @Override
    protected int getOpenDoorId()
    {
        return Constant.Objects.Door.LUMBERIDGE_SPINNER_ROOM_DOOR_OPEN;
    }
}
