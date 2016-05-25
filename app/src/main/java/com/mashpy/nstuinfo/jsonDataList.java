package com.mashpy.nstuinfo;

public class jsonDataList {
     String root_path, url;
    int menu_version;

    public jsonDataList(String root_path, int menu_version, String url) {
        this.root_path = root_path;
        this.menu_version = menu_version;
        this.url = url;
    }
    public int getmenu_version() {
        return menu_version;
    }

    public void setmenu_version(int menu_version) {
        this.menu_version = menu_version;
    }
    public String getroot_path() {
        return root_path;
    }
    public void setroot_path(String name) {
        this.root_path = name;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url= url;
    }
}
