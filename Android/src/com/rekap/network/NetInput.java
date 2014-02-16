package com.rekap.network;

import com.protoscratch.common.ScratchEvents;
import com.protoscratch.common.ScratchMessage;

public final class NetInput {

    private NetInput() { }

    public static void SendKeycode(int KeyCode)
    {
        ScratchMessage message = new ScratchMessage(NetCommands.Keyboard,
                NetCommands.SendKey);
        message.pack(KeyCode);
        SendMessage(message);
    }

    public static void SendKeySequence(String Sequence)
    {
        ScratchMessage message = new ScratchMessage(NetCommands.Keyboard,
                NetCommands.SendString);
        message.pack(Sequence);
        SendMessage(message);
    }

    public static void MoveMouse(int X, int Y) {
        ScratchMessage message = new ScratchMessage(NetCommands.Mouse,
                NetCommands.Move);
        message.pack(X);
        message.pack(Y);
        SendMessage(message);
    }

    public static void LeftClick() {
        SendMessage(NetCommands.Mouse, NetCommands.LeftClick);
    }

    public static void RightClick() {
        SendMessage(NetCommands.Mouse, NetCommands.RightClick);
    }

    public static void LeftDown() {
        SendMessage(NetCommands.Mouse, NetCommands.LeftDown);
    }

    public static void LeftUp() {
        SendMessage(NetCommands.Mouse, NetCommands.LeftUp);
    }

    public static void RightDown() {
        SendMessage(NetCommands.Mouse, NetCommands.RightDown);
    }

    public static void RightUp() {
        SendMessage(NetCommands.Mouse, NetCommands.RightUp);
    }

    public static void MiddleDown() {
        SendMessage(NetCommands.Mouse, NetCommands.MiddleDown);
    }

    public static void MiddleUp() {
        SendMessage(NetCommands.Mouse, NetCommands.MiddleUp);
    }

    public static void ScrollDown() {
        SendMessage(NetCommands.Mouse, NetCommands.ScrollDown);
    }

    public static void ScrollUp() {
        SendMessage(NetCommands.Mouse, NetCommands.ScrollUp);
    }

    public static void VolumeDown() {
        SendMessage(NetCommands.Mixer, NetCommands.VolumeDown);
    }

    public static void VolumeUp() {
        SendMessage(NetCommands.Mixer, NetCommands.VolumeUp);
    }

    private static void SendMessage(short Primary, short Secondary) {
        SendMessage(new ScratchMessage(Primary, Secondary));
    }

    private static void SendMessage(ScratchMessage message) {
        ScratchEvents client = Network.getClient();
        if (client != null) {
            client.Send(message);
        } else
            Network.Connect();
    }
}
