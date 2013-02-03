package org.cowboycoders.turbotrainers.bushido.headunit;


import java.util.HashMap;
import java.util.Map;

import org.cowboycoders.ant.events.BroadcastListener;
import org.cowboycoders.ant.messages.data.AcknowledgedDataMessage;
import org.cowboycoders.ant.messages.data.BroadcastDataMessage;
import org.cowboycoders.turbotrainers.bushido.headunit.BushidoButtonPressDescriptor.Button;

import static org.cowboycoders.ant.utils.ArrayUtils.*;

class BushidoInternalButtonPressListener implements BroadcastListener<AcknowledgedDataMessage> {
    Byte[] data;
    public static Byte[] BUTTON_PRESS_SIGNATURE = {(byte) 0xdd, 0x10};
    public static int BUTTON_PRESS_INDEX = 2;
    private BushidoInternalListener bushidoListener;
    
    public BushidoInternalButtonPressListener(BushidoInternalListener bushidoListener) {
      this.bushidoListener = bushidoListener;
      initDispatcherMap();
    }
    
    private Map<Button,ButtonPressDispatcher> dispatcherMap= new HashMap<Button,ButtonPressDispatcher>();
    
    public void initDispatcherMap() {
      for (Button b : Button.values()) {
        dispatcherMap.put(b, new ButtonPressDispatcher(bushidoListener));
      }
    }
    
    
    @Override
    public void receiveMessage(AcknowledgedDataMessage message) {
        data = message.getData();
        if (arrayStartsWith(BUTTON_PRESS_SIGNATURE, data)) {
          BushidoButtonPressDescriptor descriptor = BushidoButtonPressDescriptor.fromByte(data[BUTTON_PRESS_INDEX]);
          if (descriptor != null) {
            dispatcherMap.get(descriptor.getButton()).submitButtonPress(descriptor);
          }
        }
    }
    

}