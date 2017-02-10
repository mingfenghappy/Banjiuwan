package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.sobey.common.utils.StrUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DomainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText edit_domain;
    private CheckBox check_domain_vali;
    private CheckBox check_domain_toast;

    private ListView listView;
    private ListAdapterDomain adapter;
    ;
    private List<Domain> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list);
        adapter = new ListAdapterDomain(this, results);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        edit_domain = (EditText) findViewById(R.id.edit_domain);
        check_domain_vali = (CheckBox) findViewById(R.id.check_domain_vali);
        check_domain_toast = (CheckBox) findViewById(R.id.check_domain_toast);

        findViewById(R.id.btn_go).setOnClickListener(this);
    }

    private void initData() {
        results.clear();
        results.add(new Domain("101.201.222.160:8101", "(外网测试服务器一)"));
        results.add(new Domain("139.129.111.76:8101", "(外网测试服务器二)"));
        results.add(new Domain("192.168.118.110:9528", "(内部测试服务器)"));
        results.add(new Domain("192.168.118.205:9527", "(开发服务器：谢启谋)"));
        results.add(new Domain("192.168.118.184:8080", "(开发服务器：李作焕)"));
    }

    private void initCtrl() {
        AppData.Config.showVali = true;
        AppData.Config.showTestToast = true;
        AppData.Config.showFreshBtn = true;
        AppData.Config.needNeardriver = true;
        check_domain_vali.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppData.Config.showVali = isChecked;
            }
        });
        check_domain_toast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppData.Config.showTestToast = isChecked;
            }
        });
        String domain = AppData.App.getDomain();
        if (!StrUtils.isEmpty(domain)) {
            edit_domain.setText(domain);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Domain domain = results.get(position);
        edit_domain.setText(domain.getIp());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_go) {
            String domain = edit_domain.getText().toString();
            if (!StrUtils.isEmpty(domain)) {
                AppData.Url.domain = "http://" + domain + "/Carpooling/";
                modify();
                AppData.App.saveDomain(domain);

                Intent intent = new Intent(this, LoadUpActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void modify() {
        try {
            Object o = AppData.Url.class.newInstance();//获取对象
            Field[] field = AppData.Url.class.getFields();//获取全部参数
            for (int i = 0; i < field.length; i++) {
                Field f = field[i];
                if ((Modifier.STATIC + Modifier.PUBLIC) == f.getModifiers()) {//获取字段的修饰符，public 1,static 8
                    if (f.getType().getName().indexOf("String") != -1) {
                        String strtemp = (String) f.get(o);
                        f.set(o, replace(AppData.Url.domain, strtemp));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String replace(String domain, String str) {
        int index = str.indexOf("Carpooling/");
        if (str.endsWith("Carpooling/")) {
            return domain;
        } else {
            String substring = str.substring(index + "Carpooling/".length());
            return domain + substring;
        }
    }


    ////////////////////////////////
    /////////    列表适配器
    ////////////////////////////////

    public class ListAdapterDomain extends BaseAdapter {
        private Context context;
        private List<Domain> results;

        public List<Domain> getResults() {
            return results;
        }

        LayoutInflater inflater;

        public ListAdapterDomain(Context context, List<Domain> results) {
            this.context = context;
            this.results = results;
            if (this.results == null) {
                this.results = new ArrayList<>();
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return results.get(position);
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListAdapterDomain.ViewHolder hoder = null;
            if (convertView == null) {
                inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.item_list_domain, parent, false);
                hoder = new ListAdapterDomain.ViewHolder();
                hoder.text_ip = (TextView) convertView.findViewById(R.id.text_domain_ip);
                hoder.text_name = (TextView) convertView.findViewById(R.id.text_domain_name);
                convertView.setTag(hoder);
            } else {
                hoder = (ListAdapterDomain.ViewHolder) convertView.getTag();
            }
            Domain domain = results.get(position);

            hoder.text_ip.setText(domain.getIp());
            hoder.text_name.setText(domain.getName());

            return convertView;
        }

        public class ViewHolder {
            public TextView text_ip;
            public TextView text_name;
        }

    }

    ////////////////////////////////
    /////////    domain实体
    ////////////////////////////////

    public class Domain implements Serializable {
        private String ip;
        private String name;

        public Domain(String ip, String name) {
            this.ip = ip;
            this.name = name;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
