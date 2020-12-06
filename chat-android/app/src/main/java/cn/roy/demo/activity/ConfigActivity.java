package cn.roy.demo.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.R;
import cn.roy.demo.model.RegisterBean;
import cn.roy.demo.util.LogUtil;
import cn.roy.demo.util.SPUtil;
import cn.roy.demo.util.http.HttpUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/1/30 15:47
 * @Version: v1.0
 */
public class ConfigActivity extends BaseActivity {
    private LinearLayout ll_main;
    private Toolbar toolbar;
    private LinearLayout ll_socket_config;
    private EditText et_api_host, et_api_port, et_socket_host, et_socket_port;
    private SwitchCompat switchCompat;
    private Button btn_submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        ll_main = findViewById(R.id.ll_main);
        toolbar = findViewById(R.id.toolbar);
        et_api_host = findViewById(R.id.et_api_host);
        et_api_port = findViewById(R.id.et_api_port);
        switchCompat = findViewById(R.id.switchCompat);
        ll_socket_config = findViewById(R.id.ll_socket_config);
        et_socket_host = findViewById(R.id.et_socket_host);
        et_socket_port = findViewById(R.id.et_socket_port);
        btn_submit = findViewById(R.id.btn_submit);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_socket_config.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apiHost = et_api_host.getText().toString().trim();
                if (TextUtils.isEmpty(apiHost)) {
                    toast("api服务器host不能为空");
                    return;
                }
                String apiPort = et_api_port.getText().toString().trim();
                if (TextUtils.isEmpty(apiPort)) {
                    toast("api服务器port不能为空");
                    return;
                }
                String socketHost = et_socket_host.getText().toString().trim();
                String socketPort = et_socket_port.getText().toString().trim();
                if (switchCompat.isChecked()) {
                    if (TextUtils.isEmpty(socketHost)) {
                        toast("聊天服务器host不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(socketPort)) {
                        toast("聊天服务器port不能为空");
                        return;
                    }
                }
                Config config = new Config();
                config.setApiHost(apiHost);
                config.setApiPort(Integer.parseInt(apiPort));
                config.setSocketCustom(switchCompat.isChecked());
                config.setSocketHost(socketHost);
                config.setSocketPort(Integer.parseInt(socketPort));
                save(config);
            }
        });

        initData();
    }

    private String hostOfApi;
    private int portOfApi;
    private boolean isCustomSocket = false;
    private String hostOfSocket;
    private int portOfSocket;

    private void initData() {
        hostOfApi = SPUtil.getString(ApplicationConfig.ServerConfig.HOST_OF_API,
                "");
        portOfApi = SPUtil.getInt(ApplicationConfig.ServerConfig.PORT_OF_API,
                9001);
        isCustomSocket = SPUtil.getBoolean(ApplicationConfig.ServerConfig.IS_CUSTOM_SOCKET,
                false);
        hostOfSocket = SPUtil.getString(ApplicationConfig.ServerConfig.HOST_OF_SOCKET,
                "");
        portOfSocket = SPUtil.getInt(ApplicationConfig.ServerConfig.PORT_OF_SOCKET,
                2000);
        String[] split = ApplicationConfig.HttpConfig.API_BASE_URL.split(":");
        if (TextUtils.isEmpty(hostOfApi)) {
            hostOfApi = split[0];
            portOfApi = Integer.parseInt(split[1]);
        }

        et_api_host.setText(hostOfApi);
        et_api_port.setText(String.valueOf(portOfApi));
        switchCompat.setChecked(isCustomSocket);
        ll_socket_config.setVisibility(isCustomSocket ? View.VISIBLE : View.GONE);
        et_socket_host.setText(String.valueOf(hostOfSocket));
        et_socket_port.setText(String.valueOf(portOfSocket));
    }

    private void save(Config config) {
        SPUtil.saveParam(ApplicationConfig.ServerConfig.HOST_OF_API, config.getApiHost());
        SPUtil.saveParam(ApplicationConfig.ServerConfig.PORT_OF_API, config.getApiPort());
        SPUtil.saveParam(ApplicationConfig.ServerConfig.IS_CUSTOM_SOCKET, config.isSocketCustom());
        SPUtil.saveParam(ApplicationConfig.ServerConfig.HOST_OF_SOCKET, config.getSocketHost());
        SPUtil.saveParam(ApplicationConfig.ServerConfig.PORT_OF_SOCKET, config.getSocketPort());
        ApplicationConfig.HttpConfig.API_BASE_URL = config.getApiHost() + ":" + config.getApiPort();
        finish();
    }

    private class Config {
        private String apiHost;
        private int apiPort;
        private boolean socketCustom;
        private String socketHost;
        private int socketPort;

        public String getApiHost() {
            return apiHost;
        }

        public void setApiHost(String apiHost) {
            this.apiHost = apiHost;
        }

        public int getApiPort() {
            return apiPort;
        }

        public void setApiPort(int apiPort) {
            this.apiPort = apiPort;
        }

        public boolean isSocketCustom() {
            return socketCustom;
        }

        public void setSocketCustom(boolean socketCustom) {
            this.socketCustom = socketCustom;
        }

        public String getSocketHost() {
            return socketHost;
        }

        public void setSocketHost(String socketHost) {
            this.socketHost = socketHost;
        }

        public int getSocketPort() {
            return socketPort;
        }

        public void setSocketPort(int socketPort) {
            this.socketPort = socketPort;
        }
    }

}
