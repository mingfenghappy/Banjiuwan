package com.ins.feast.common;

import com.sobey.common.utils.StrUtils;
import com.sobey.common.utils.ValidateUtil;
import com.sobey.common.view.bundleimgview.BundleImgEntity;

import java.util.List;

/**
 * 输入验证类，封装了app中所有需要验证输入的方法
 *
 * @author Administrator
 */
public class AppVali {

    private static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    private static boolean length(String str, int min, int max) {
        return str.length() >= min && str.length() <= max;
    }

    public static String novali() {
        return null;
    }

    public static String login_go(String name, String psw) {
        if (isEmpty(name)) {
            return "请输入用户名";
        } else if (isEmpty(psw)) {
            return "请输入密码";
        } else if (!length(psw, 6, 16)) {
            return "密码长度必须为6-16位";
        } else if (ValidateUtil.Empty(psw)){
            return "密码不能包含空格";
        }else {
            return null;
        }
    }

    public static String valicode(String valicode, String vali) {
        if (isEmpty(vali)) {
            return "请输入验证码";
        } else if (!vali.equals(valicode)) {
            return "验证码错误";
        } else {
            return null;
        }
    }

    public static String vali_identify_passenger(String[] paths, String realName, String idcardnum) {
        if (isEmpty(realName)) {
            return "请输入真实姓名";
        } else if (isEmpty(idcardnum)) {
            return "请输入身份证号";
        } else if (!ValidateUtil.IDcard(idcardnum)) {
            return "身份证号格式不正确";
        } else if (StrUtils.isEmpty(paths)) {
            return "验证失败";
        } else {
            if (StrUtils.isEmpty(paths[0])) {
                return "请上传身份证正面照";
            } else if (StrUtils.isEmpty(paths[1])) {
                return "请上传身份证反面照";
            } else {
                return null;
            }
        }
    }

    public static String vali_identify_driverthree(String path, String drivernum) {
        if (isEmpty(drivernum)) {
            return "请输入驾驶证证号";
        } else if (!ValidateUtil.IDcard(drivernum)) {
            return "驾驶证证号格式不正确";
        } else if (isEmpty(path)) {
            return "拍摄驾驶证正面照";
        } else {
            return null;
        }
    }

    public static String vali_identify_drivertwo(String path, String carnum, String cartype, String carcolor, String carowner, String travelcardnum) {
        if (isEmpty(carnum)) {
            return "请输入车牌号";
        } else if (isEmpty(cartype)) {
            return "请输入车辆型号";
        } else if (isEmpty(carcolor)) {
            return "请输入车辆颜色";
        } else if (isEmpty(carowner)) {
            return "请输入车辆所有人";
        }
        //取消了行驶证
        //else if (isEmpty(travelcardnum)) {
        //    return "请输入行驶证号";
        //}
        else if (isEmpty(path)) {
            return "拍摄行驶证正面照";
        } else {
            return null;
        }
    }

    public static String evaadd(float start, String detail) {
        if (start == 0) {
            return "您还没有打分";
        } else {
            return null;
        }
    }

    public static String complainadd(String ids, String detail) {
        if (StrUtils.isEmpty(ids)) {
            return "请选择投诉项目";
        } else if (StrUtils.isEmpty(detail)) {
            return "请填写投诉内容";
        } else {
            return null;
        }
    }

    public static String regist_phone(String phone_edit, String phone, String vali, String valicode) {
        if (isEmpty(phone_edit)) {
            return "请输入手机号";
        } else if (!phone_edit.equals(phone)) {
            return "你输入的号码没有验证过";
        } else if (!ValidateUtil.Mobile(phone)) {
            return "请输入正确的手机号";
        } else if (!vali.equals(valicode)) {
            return "验证码不正确";
        } else {
            return null;
        }
    }

    public static String delBankCard(int cardId, String psw) {
        if (isEmpty(psw)) {
            return "请输入提现密码";
        } else if (psw.length() != 6) {
            return "请输入完整的提现密码";
        } else if (cardId == 0) {
            return "银行卡不存在 id:0";
        } else {
            return null;
        }
    }

    public static String payPsw(String psw) {
        if (isEmpty(psw)) {
            return "请输入提现密码";
        } else if (psw.length() != 6) {
            return "请输入完整的提现密码";
        } else {
            return null;
        }
    }

    public static String payPsw(String psw, String repetpsw) {
        if (isEmpty(repetpsw)) {
            return "请输入提现密码";
        } else if (repetpsw.length() != 6) {
            return "请输入完整的提现密码";
        } else if (!repetpsw.equals(psw)) {
            return "两次输入密码不一致";
        } else {
            return null;
        }
    }

    public static String vali(String vali, String valicode) {
        if (isEmpty(vali)) {
            return "请输入验证码";
        } else if (!vali.equals(valicode)) {
            return "验证码不正确";
        } else {
            return null;
        }
    }

    public static String modifypswuser(String vali, String valicode, String psw, String repetpsw) {
        if (isEmpty(psw)) {
            return "请输入新密码";
        } else if (!length(psw, 6, 16)) {
            return "密码长度必须为6-16位";
        }  else if (ValidateUtil.Empty(psw)){
            return "密码不能包含空格";
        } else if (isEmpty(repetpsw)) {
            return "请再次输入新密码";
        } else if (!psw.equals(repetpsw)) {
            return "两次输入密码不一致";
        } else if (isEmpty(vali)) {
            return "请输入验证码";
        } else if (!vali.equals(valicode)) {
            return "验证码不正确";
        } else {
            return null;
        }
    }

    public static String addBankCard(String phone_edit, String phone, String vali, String valicode, String bankNum, String bankName) {
        if (isEmpty(phone_edit)) {
            return "请输入手机号";
        } else if (!phone_edit.equals(phone)) {
            return "你输入的号码没有验证过";
        } else if (!ValidateUtil.Mobile(phone)) {
            return "请输入正确的手机号";
        } else if (!vali.equals(valicode)) {
            return "验证码不正确";
        } else if (!length(bankNum, 6, 21)) {
            return "银行卡号不正确";
        } else if (isEmpty(bankName)) {
            return "银行名称不正确";
        } else {
            return null;
        }
    }

    public static String phone(String phone) {
        if (isEmpty(phone)) {
            return "请输入手机号";
        } else if (!ValidateUtil.Mobile(phone)) {
            return "请输入正确的手机号";
        } else {
            return null;
        }
    }

    public static String bank(String bank) {
        if (isEmpty(bank)) {
            return "请输入银行卡号";
        } else if (!length(bank, 6, 21)) {
            return "请输入正确的银行卡号";
        } else {
            return null;
        }
    }

    public static String find_psw(String phone_edit, String phone, String vali, String valicode, String psw, String newpsw) {
        if (isEmpty(phone_edit)) {
            return "请输入手机号";
        } else if (!phone_edit.equals(phone)) {
            return "你输入的号码没有验证过";
        } else if (!ValidateUtil.Mobile(phone)) {
            return "请输入正确的手机号";
        } else if (!vali.equals(valicode)) {
            return "验证码不正确";
        } else if (!length(psw, 6, 16)) {
            return "密码长度必须为6-16位";
        } else if (!psw.equals(newpsw)) {
            return "两次输入密码不一致";
        } else {
            return null;
        }
    }

    public static String modify_psw(String psw_old, String psw_new, String psw_new_repeat) {
        if (isEmpty(psw_old)) {
            return "请输入旧密码";
        } else if (isEmpty(psw_new)) {
            return "你输入新密码";
        } else if (isEmpty(psw_new_repeat)) {
            return "你确认新密码";
        } else if (!length(psw_old, 6, 16)) {
            return "旧密码长度必须为6-16位";
        } else if (!length(psw_new, 6, 16)) {
            return "新密码长度必须为6-16位";
        } else if (!psw_new.equals(psw_new_repeat)) {
            return "两次输入密码不一致";
        } else {
            return null;
        }
    }

    public static String orderadd(int deviceId, String contract, String phone, String supply, String describe, List<BundleImgEntity> results, int orderType) {
        if (deviceId == 0) {
            if (orderType == 0) {
                return "请选择需要维修的设备";
            } else if (orderType == 1) {
                return "请选择需要保养的零件";
            } else {
                return "error:type=" + orderType;
            }
        } else if (results == null || results.size() < 3) {
            return "请至少上传3张图片";
        } else if (isEmpty(contract)) {
            return "请填写联系人";
        } else if (isEmpty(phone)) {
            return "请填写联系人电话";
        } else if (isEmpty(supply)) {
            return "请填写供应商";
        } else if (isEmpty(describe)) {
            return "请填写描述";
        } else {
            return null;
        }
    }

    public static String orderDescribe(String timeStr, String describe, int flag) {
        if (isEmpty(timeStr) && flag != 2) {
            //flag 2 :维修完成 ，不需要计算时间
            if (flag == 0) {
                return "请选择预计到达时间";
            } else {
                return "请选择预计完成时间";
            }
        } else if (isEmpty(describe)) {
            return "请填写订单进度描述";
        } else {
            return null;
        }
    }

    public static String feedback(String describe) {
        if (isEmpty(describe)) {
            return "描述不能为空";
        } else {
            return null;
        }
    }

    public static String regist_detail(String name, String password, String password_repet, String mail, int officeId) {
        if (isEmpty(name)) {
            return "请输入姓名";
        } else if (isEmpty(password)) {
            return "请输入登录密码";
        } else if (isEmpty(mail)) {
            return "请输入邮箱";
        } else if (officeId == 0) {
            return "请选择所属单位";
        } else if (!length(password, 6, 16)) {
            return "密码长度必须为6-16位";
        } else if (!password.equals(password_repet)) {
            return "确认密码输入不一致";
        } else if (!ValidateUtil.Email(mail)) {
            return "输入邮箱格式不正确";
        }else {
            return null;
        }
    }

    public static String reqfix_commit(int categoryId, String detail) {
        if (categoryId == 0) {
            return "请选择问题分类";
        } else if (isEmpty(detail)) {
            return "请输入问题描述";
        } else {
            return null;
        }
    }

    public static String reqfix_commit_withuser(int categoryId, int userId, String detail) {
        if (categoryId == 0) {
            return "请选择问题分类";
        } else if (userId == 0) {
            return "请选择代理申报用户";
        } else if (isEmpty(detail)) {
            return "请输入问题描述";
        } else {
            return null;
        }
    }

    public static String reqfix_addDescribe(String detail) {
        if (isEmpty(detail)) {
            return "请输入描述";
        } else {
            return null;
        }
    }

    public static String reqfix_addDescribe_withuser(int userId, String detail) {
        if (userId == 0) {
            return "请选择援助对象";
        } else if (isEmpty(detail)) {
            return "请输入问题描述";
        } else {
            return null;
        }
    }

}
