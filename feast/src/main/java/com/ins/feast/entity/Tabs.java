package com.ins.feast.entity;

import android.support.annotation.IdRes;

import com.ins.feast.R;
import com.ins.middle.base.TitleHelper;
import com.ins.middle.common.AppData;

/**
 * author 边凌
 * date 2017/3/9 14:26
 * desc ${主页Tab的相关信息常量类}
 */

public enum Tabs {
    home {
        @Override
        public String getTitle() {
            return "办酒碗";
        }

        @Override
        public int getButtonId() {
            return R.id.rb_home;
        }

        @Override
        public String getUrlTag() {
            return "index";
        }

        @Override
        public TitleHelper.TitleType getTitleType() {
            return TitleHelper.TitleType.home;
        }

        @Override
        public String getUrl() {
            return AppData.Url.app_home;
        }
    },
    car {
        @Override
        public String getTitle() {
            return "购物车";
        }

        @Override
        public int getButtonId() {
            return R.id.rb_cart;
        }

        @Override
        public String getUrlTag() {
            return "car";
        }

        @Override
        public TitleHelper.TitleType getTitleType() {
            return TitleHelper.TitleType.onlyCenter;
        }

        @Override
        public String getUrl() {
            return AppData.Url.app_cart;
        }
    },
    find {
        @Override
        public String getTitle() {
            return "发现";
        }

        @Override
        public int getButtonId() {
            return R.id.rb_find;
        }

        @Override
        public String getUrlTag() {
            return "find";
        }

        @Override
        public TitleHelper.TitleType getTitleType() {
            return TitleHelper.TitleType.onlyCenter;
        }

        @Override
        public String getUrl() {
            return AppData.Url.app_find;
        }
    },
    custom {
        @Override
        public String getTitle() {
            return "";
        }

        @Override
        public int getButtonId() {
            return R.id.rb_customerService;
        }

        @Override
        public String getUrlTag() {
            return "customer";
        }

        @Override
        public TitleHelper.TitleType getTitleType() {
            return TitleHelper.TitleType.noTitle;
        }

        @Override
        public String getUrl() {
            return AppData.Url.app_customer_service;
        }
    },
    mine {
        @Override
        public String getTitle() {
            return "我的";
        }

        @Override
        public int getButtonId() {
            return R.id.rb_mine;
        }

        @Override
        public String getUrlTag() {
            return "my";
        }

        @Override
        public TitleHelper.TitleType getTitleType() {
            return TitleHelper.TitleType.onlyCenter;
        }

        @Override
        public String getUrl() {
            return AppData.Url.app_mine;
        }
    };

    public abstract String getTitle();

    public abstract
    @IdRes
    int getButtonId();

    public abstract String getUrlTag();

    public abstract TitleHelper.TitleType getTitleType();

    public abstract String getUrl();
}
