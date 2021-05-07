package com.txt.video.net.bean;

import java.util.List;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/21.
 * description：
 */
public class LoginBean {

    /**
     * agent : {"skillName":["保全","理赔"],"_id":"5ee88000c488564bd8621344","createdAt":"2020-06-16T08:17:04.682Z","updatedAt":"2020-06-16T08:17:04.682Z","loginName":"wjqdev","creator":"5ebcf4288444541f383e9b26","fullName":"王健泉","cellphone":"135000022222","idCard":"36252619940529471X","agentRole":{"_id":"5d5d0773d98a3e6cdd3a969f","roleName":"坐席","level":"1","utime":"2019-08-21T08:57:23.904Z","ctime":"2019-08-21T08:57:23.904Z","__v":0,"menus":[{"key":"/workBench","pageName":"工作台","iconType":"home","subMenu":[{"iconType":"","pageName":"工单列表","key":"/workOrders"},{"iconType":"","pageName":"通话历史","key":"/callRecord"}]}]},"tenant":{"_id":"5ec49d1f52efce001817b1b2","ctime":"2020-05-20T02:59:43.302Z","utime":"2020-06-16T02:33:45.075Z","creator":"5ec49afe8444541f383e9b29","name":"功能开发租户","phone":"18911111111","bucket":"gdrb-dingsun-test","showInsuranceName":"险种","transTime":5,"validDate":"2022-07-09T15:59:59.999Z","maxAgents":100,"code":"45","remark":"","logo":"https://s3.cn-north-1.amazonaws.com.cn/gdrb-dingsun-test/20200527/1590546697126.jpg","__v":1,"callSkill":"taskType","reportListsApp":[{"key":"insurance","key2":"name","name":"产品","clientKey":"insuranceName","type":"twice","dataMasking":[]},{"key":"insurantName","name":"投保人","clientKey":"insurantName","type":"fields","dataMasking":[]},{"key":"insurantPhone","name":"联系电话","clientKey":"insurantPhone","type":"fields","dataMasking":[]},{"key":"insurantIdCard","name":"身份证号","clientKey":"insurantIdCard","type":"fields","dataMasking":[]},{"key":"insuredMoney","name":"保额","clientKey":"insuredMoney","type":"fields","dataMasking":[]},{"key":"insuredDate","name":"保险期限","clientKey":"insuredDate","type":"fields","dataMasking":[]},{"key":"payType","name":"交费方式","clientKey":"payType","type":"fields","dataMasking":[]},{"key":"state","name":"状态","clientKey":"state","type":"state","dataMasking":[]},{"key":"ctime","name":"创建时间","clientKey":"ctime","type":"time","dataMasking":[]}],"reportLists":[{"key":"insurance","key2":"name","name":"产品","clientKey":"insuranceName","type":"twice","dataMasking":[]},{"key":"flowId","name":"工单号","clientKey":"flowId","type":"string","dataMasking":[]},{"key":"reportId","name":"业务号","clientKey":"reportId","type":"fields","dataMasking":[]},{"key":"insurantName","name":"投保人","clientKey":"insurantName","type":"fields","dataMasking":[]},{"key":"insurantPhone","name":"联系电话","clientKey":"insurantPhone","type":"fields","dataMasking":[]},{"key":"agentId","key2":"fullName","name":"处理人","clientKey":"agentName","type":"twice","dataMasking":[]},{"key":"state","name":"状态","clientKey":"state","type":"state","dataMasking":[]},{"key":"ctime","name":"创建时间","clientKey":"ctime","type":"time","dataMasking":[]},{"key":"recordUploadTime","name":"上传时间","clientKey":"recordUploadTime","type":"time","dataMasking":[]},{"key":"recordDuration","name":"录制时长","clientKey":"recordDuration","type":"duration","dataMasking":[]}],"reportStates":[{"_id":"5e0d8eab37fc9823c43c689d","code":"untreated","name":"待录制","color":"#FFFFFF","__v":0,"backgroundColor":"#00965E"},{"_id":"5bc9b5f648496200065dfb1d","code":"checking","name":"待上传","color":"#FFFFFF","__v":0,"backgroundColor":"#FF0000"},{"_id":"5bc9b60148496200065dfb1e","code":"checked","name":"已完成","color":"#FFFFFF","__v":0,"backgroundColor":"#7F7F7F"}],"reportQueries":[{"key":"ctime","isFields":false,"name":"日期","placeholder":"日期","type":"rangePicker","mongoType":"Date","style":{"width":216},"allowClear":true,"defaultKey":"5ee88000c488564bd8621344","options":[{"name":"测试租户机构坐席1","key":"5ec4eff2335c0f00221e066f"},{"name":"测试租户机构坐席2","key":"5ec4f651a4f25b0022f7763a"},{"name":"开发","key":"5edf39dd51d956456eee31d0"},{"name":"王健泉","key":"5ee88000c488564bd8621344"},{"name":"刘江","key":"5ee884c5c488564bd862134c"}]},{"defaultKey":"5ee88000c488564bd8621344","options":[{"name":"测试租户机构坐席1","key":"5ec4eff2335c0f00221e066f"},{"name":"测试租户机构坐席2","key":"5ec4f651a4f25b0022f7763a"},{"name":"开发","key":"5edf39dd51d956456eee31d0"},{"name":"王健泉","key":"5ee88000c488564bd8621344"},{"name":"刘江","key":"5ee884c5c488564bd862134c"}],"key":"agentId","isFields":false,"name":"处理人","placeholder":"处理人","type":"select","mongoType":"ObjectId","style":{"width":216},"allowClear":true},{"key":"reportId","isFields":true,"name":"业务号","placeholder":"请输入业务号","type":"input","mongoType":"String","style":{"width":216},"allowClear":true},{"key":"insurantName","isFields":true,"name":"投保人","placeholder":"请输入投保人姓名","type":"input","mongoType":"String","style":{"width":216},"allowClear":true}],"showInsurance":true,"smsContent":[{"content":"尊敬的{insurantName}，我司保险代理人邀请您远程视频办理保险业务，请您点击以下链接http://ins-online.ikandy.cn/ 进入视频。如有疑问请致电95XXX或联系您的保单服务人员。感谢您的配合，祝您生活愉快！【甜新科技】","phone":"insurantPhone","_id":"5ed5bc909614cc00182a7165"},{"content":"尊敬的{insuredName}，我司保险代理人邀请您远程视频办理保险业务，请您点击以下链接http://ins-online.ikandy.cn/ 进入视频。如有疑问请致电95XXX或联系您的保单服务人员。感谢您的配合，祝您生活愉快！【甜新科技】","phone":"insuredPhone","_id":"5ed5bc909614cc00182a7166"}],"status":true},"orgAccount":{"_id":"5ec4ecfca4f25b0022f77608","createdAt":"2020-05-20T08:40:28.042Z","updatedAt":"2020-05-20T09:21:04.729Z","orgName":"测试租户--机构1","tenant":"5ec49d1f52efce001817b1b2","code":"123456","__v":2,"workingDayTime":[{"from":"10:30:00","end":"11:00:00","_id":"5ec4f680a4f25b0022f7763c"},{"from":"16:00:00","end":"18:00:00","_id":"5ec4f680a4f25b0022f7763b"}],"callDealing":false,"status":true},"accountStatus":true,"departments":[{"_id":"5ec4f3aba4f25b0022f7762c","skills":["POSA","CLAIM"]}]}
     * sysRoles : [{"_id":"5d5d0773d98a3e6cdd3a969f","roleName":"坐席","level":"1"}]
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbk5hbWUiOiJ3anFkZXYiLCJwYXNzd29yZCI6IjEyMzQ1NiIsImlhdCI6MTU5Nzk3OTgwMSwiZXhwIjoxNTk4MDY2MjAxfQ.wWwFdTsTmjqdh28coRYIZd_6HnsoeqbHxr6lxrwo6cI
     */

    private AgentBean agent;
    private String token;
    private List<SysRolesBean> sysRoles;

    public AgentBean getAgent() {
        return agent;
    }

    public void setAgent(AgentBean agent) {
        this.agent = agent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<SysRolesBean> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<SysRolesBean> sysRoles) {
        this.sysRoles = sysRoles;
    }

    public static class AgentBean {
        /**
         * skillName : ["保全","理赔"]
         * _id : 5ee88000c488564bd8621344
         * createdAt : 2020-06-16T08:17:04.682Z
         * updatedAt : 2020-06-16T08:17:04.682Z
         * loginName : wjqdev
         * creator : 5ebcf4288444541f383e9b26
         * fullName : 王健泉
         * cellphone : 135000022222
         * idCard : 36252619940529471X
         * agentRole : {"_id":"5d5d0773d98a3e6cdd3a969f","roleName":"坐席","level":"1","utime":"2019-08-21T08:57:23.904Z","ctime":"2019-08-21T08:57:23.904Z","__v":0,"menus":[{"key":"/workBench","pageName":"工作台","iconType":"home","subMenu":[{"iconType":"","pageName":"工单列表","key":"/workOrders"},{"iconType":"","pageName":"通话历史","key":"/callRecord"}]}]}
         * tenant : {"_id":"5ec49d1f52efce001817b1b2","ctime":"2020-05-20T02:59:43.302Z","utime":"2020-06-16T02:33:45.075Z","creator":"5ec49afe8444541f383e9b29","name":"功能开发租户","phone":"18911111111","bucket":"gdrb-dingsun-test","showInsuranceName":"险种","transTime":5,"validDate":"2022-07-09T15:59:59.999Z","maxAgents":100,"code":"45","remark":"","logo":"https://s3.cn-north-1.amazonaws.com.cn/gdrb-dingsun-test/20200527/1590546697126.jpg","__v":1,"callSkill":"taskType","reportListsApp":[{"key":"insurance","key2":"name","name":"产品","clientKey":"insuranceName","type":"twice","dataMasking":[]},{"key":"insurantName","name":"投保人","clientKey":"insurantName","type":"fields","dataMasking":[]},{"key":"insurantPhone","name":"联系电话","clientKey":"insurantPhone","type":"fields","dataMasking":[]},{"key":"insurantIdCard","name":"身份证号","clientKey":"insurantIdCard","type":"fields","dataMasking":[]},{"key":"insuredMoney","name":"保额","clientKey":"insuredMoney","type":"fields","dataMasking":[]},{"key":"insuredDate","name":"保险期限","clientKey":"insuredDate","type":"fields","dataMasking":[]},{"key":"payType","name":"交费方式","clientKey":"payType","type":"fields","dataMasking":[]},{"key":"state","name":"状态","clientKey":"state","type":"state","dataMasking":[]},{"key":"ctime","name":"创建时间","clientKey":"ctime","type":"time","dataMasking":[]}],"reportLists":[{"key":"insurance","key2":"name","name":"产品","clientKey":"insuranceName","type":"twice","dataMasking":[]},{"key":"flowId","name":"工单号","clientKey":"flowId","type":"string","dataMasking":[]},{"key":"reportId","name":"业务号","clientKey":"reportId","type":"fields","dataMasking":[]},{"key":"insurantName","name":"投保人","clientKey":"insurantName","type":"fields","dataMasking":[]},{"key":"insurantPhone","name":"联系电话","clientKey":"insurantPhone","type":"fields","dataMasking":[]},{"key":"agentId","key2":"fullName","name":"处理人","clientKey":"agentName","type":"twice","dataMasking":[]},{"key":"state","name":"状态","clientKey":"state","type":"state","dataMasking":[]},{"key":"ctime","name":"创建时间","clientKey":"ctime","type":"time","dataMasking":[]},{"key":"recordUploadTime","name":"上传时间","clientKey":"recordUploadTime","type":"time","dataMasking":[]},{"key":"recordDuration","name":"录制时长","clientKey":"recordDuration","type":"duration","dataMasking":[]}],"reportStates":[{"_id":"5e0d8eab37fc9823c43c689d","code":"untreated","name":"待录制","color":"#FFFFFF","__v":0,"backgroundColor":"#00965E"},{"_id":"5bc9b5f648496200065dfb1d","code":"checking","name":"待上传","color":"#FFFFFF","__v":0,"backgroundColor":"#FF0000"},{"_id":"5bc9b60148496200065dfb1e","code":"checked","name":"已完成","color":"#FFFFFF","__v":0,"backgroundColor":"#7F7F7F"}],"reportQueries":[{"key":"ctime","isFields":false,"name":"日期","placeholder":"日期","type":"rangePicker","mongoType":"Date","style":{"width":216},"allowClear":true},{"defaultKey":"5ee88000c488564bd8621344","options":[{"name":"测试租户机构坐席1","key":"5ec4eff2335c0f00221e066f"},{"name":"测试租户机构坐席2","key":"5ec4f651a4f25b0022f7763a"},{"name":"开发","key":"5edf39dd51d956456eee31d0"},{"name":"王健泉","key":"5ee88000c488564bd8621344"},{"name":"刘江","key":"5ee884c5c488564bd862134c"}],"key":"agentId","isFields":false,"name":"处理人","placeholder":"处理人","type":"select","mongoType":"ObjectId","style":{"width":216},"allowClear":true},{"key":"reportId","isFields":true,"name":"业务号","placeholder":"请输入业务号","type":"input","mongoType":"String","style":{"width":216},"allowClear":true},{"key":"insurantName","isFields":true,"name":"投保人","placeholder":"请输入投保人姓名","type":"input","mongoType":"String","style":{"width":216},"allowClear":true}],"showInsurance":true,"smsContent":[{"content":"尊敬的{insurantName}，我司保险代理人邀请您远程视频办理保险业务，请您点击以下链接http://ins-online.ikandy.cn/ 进入视频。如有疑问请致电95XXX或联系您的保单服务人员。感谢您的配合，祝您生活愉快！【甜新科技】","phone":"insurantPhone","_id":"5ed5bc909614cc00182a7165"},{"content":"尊敬的{insuredName}，我司保险代理人邀请您远程视频办理保险业务，请您点击以下链接http://ins-online.ikandy.cn/ 进入视频。如有疑问请致电95XXX或联系您的保单服务人员。感谢您的配合，祝您生活愉快！【甜新科技】","phone":"insuredPhone","_id":"5ed5bc909614cc00182a7166"}],"status":true}
         * orgAccount : {"_id":"5ec4ecfca4f25b0022f77608","createdAt":"2020-05-20T08:40:28.042Z","updatedAt":"2020-05-20T09:21:04.729Z","orgName":"测试租户--机构1","tenant":"5ec49d1f52efce001817b1b2","code":"123456","__v":2,"workingDayTime":[{"from":"10:30:00","end":"11:00:00","_id":"5ec4f680a4f25b0022f7763c"},{"from":"16:00:00","end":"18:00:00","_id":"5ec4f680a4f25b0022f7763b"}],"callDealing":false,"status":true}
         * accountStatus : true
         * departments : [{"_id":"5ec4f3aba4f25b0022f7762c","skills":["POSA","CLAIM"]}]
         */

        private String _id;
        private String createdAt;
        private String updatedAt;
        private String loginName;
        private String creator;
        private String fullName;
        private String cellphone;
        private String idCard;
        private AgentRoleBean agentRole;
        private TenantBean tenant;
        private OrgAccountBean orgAccount;
        private boolean accountStatus;
        private List<String> skillName;
        private List<DepartmentsBean> departments;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getCellphone() {
            return cellphone;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public AgentRoleBean getAgentRole() {
            return agentRole;
        }

        public void setAgentRole(AgentRoleBean agentRole) {
            this.agentRole = agentRole;
        }

        public TenantBean getTenant() {
            return tenant;
        }

        public void setTenant(TenantBean tenant) {
            this.tenant = tenant;
        }

        public OrgAccountBean getOrgAccount() {
            return orgAccount;
        }

        public void setOrgAccount(OrgAccountBean orgAccount) {
            this.orgAccount = orgAccount;
        }

        public boolean isAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(boolean accountStatus) {
            this.accountStatus = accountStatus;
        }

        public List<String> getSkillName() {
            return skillName;
        }

        public void setSkillName(List<String> skillName) {
            this.skillName = skillName;
        }

        public List<DepartmentsBean> getDepartments() {
            return departments;
        }

        public void setDepartments(List<DepartmentsBean> departments) {
            this.departments = departments;
        }

        public static class AgentRoleBean {
            /**
             * _id : 5d5d0773d98a3e6cdd3a969f
             * roleName : 坐席
             * level : 1
             * utime : 2019-08-21T08:57:23.904Z
             * ctime : 2019-08-21T08:57:23.904Z
             * __v : 0
             * menus : [{"key":"/workBench","pageName":"工作台","iconType":"home","subMenu":[{"iconType":"","pageName":"工单列表","key":"/workOrders"},{"iconType":"","pageName":"通话历史","key":"/callRecord"}]}]
             */

            private String _id;
            private String roleName;
            private String level;
            private String utime;
            private String ctime;
            private int __v;
            private List<MenusBean> menus;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getRoleName() {
                return roleName;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getUtime() {
                return utime;
            }

            public void setUtime(String utime) {
                this.utime = utime;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }

            public int get__v() {
                return __v;
            }

            public void set__v(int __v) {
                this.__v = __v;
            }

            public List<MenusBean> getMenus() {
                return menus;
            }

            public void setMenus(List<MenusBean> menus) {
                this.menus = menus;
            }

            public static class MenusBean {
                /**
                 * key : /workBench
                 * pageName : 工作台
                 * iconType : home
                 * subMenu : [{"iconType":"","pageName":"工单列表","key":"/workOrders"},{"iconType":"","pageName":"通话历史","key":"/callRecord"}]
                 */

                private String key;
                private String pageName;
                private String iconType;
                private List<SubMenuBean> subMenu;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public String getPageName() {
                    return pageName;
                }

                public void setPageName(String pageName) {
                    this.pageName = pageName;
                }

                public String getIconType() {
                    return iconType;
                }

                public void setIconType(String iconType) {
                    this.iconType = iconType;
                }

                public List<SubMenuBean> getSubMenu() {
                    return subMenu;
                }

                public void setSubMenu(List<SubMenuBean> subMenu) {
                    this.subMenu = subMenu;
                }

                public static class SubMenuBean {
                    /**
                     * iconType :
                     * pageName : 工单列表
                     * key : /workOrders
                     */

                    private String iconType;
                    private String pageName;
                    private String key;

                    public String getIconType() {
                        return iconType;
                    }

                    public void setIconType(String iconType) {
                        this.iconType = iconType;
                    }

                    public String getPageName() {
                        return pageName;
                    }

                    public void setPageName(String pageName) {
                        this.pageName = pageName;
                    }

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        this.key = key;
                    }
                }
            }
        }

        public static class TenantBean {
            /**
             * _id : 5ec49d1f52efce001817b1b2
             * ctime : 2020-05-20T02:59:43.302Z
             * utime : 2020-06-16T02:33:45.075Z
             * creator : 5ec49afe8444541f383e9b29
             * name : 功能开发租户
             * phone : 18911111111
             * bucket : gdrb-dingsun-test
             * showInsuranceName : 险种
             * transTime : 5
             * validDate : 2022-07-09T15:59:59.999Z
             * maxAgents : 100
             * code : 45
             * remark :
             * logo : https://s3.cn-north-1.amazonaws.com.cn/gdrb-dingsun-test/20200527/1590546697126.jpg
             * __v : 1
             * callSkill : taskType
             * reportListsApp : [{"key":"insurance","key2":"name","name":"产品","clientKey":"insuranceName","type":"twice","dataMasking":[]},{"key":"insurantName","name":"投保人","clientKey":"insurantName","type":"fields","dataMasking":[]},{"key":"insurantPhone","name":"联系电话","clientKey":"insurantPhone","type":"fields","dataMasking":[]},{"key":"insurantIdCard","name":"身份证号","clientKey":"insurantIdCard","type":"fields","dataMasking":[]},{"key":"insuredMoney","name":"保额","clientKey":"insuredMoney","type":"fields","dataMasking":[]},{"key":"insuredDate","name":"保险期限","clientKey":"insuredDate","type":"fields","dataMasking":[]},{"key":"payType","name":"交费方式","clientKey":"payType","type":"fields","dataMasking":[]},{"key":"state","name":"状态","clientKey":"state","type":"state","dataMasking":[]},{"key":"ctime","name":"创建时间","clientKey":"ctime","type":"time","dataMasking":[]}]
             * reportLists : [{"key":"insurance","key2":"name","name":"产品","clientKey":"insuranceName","type":"twice","dataMasking":[]},{"key":"flowId","name":"工单号","clientKey":"flowId","type":"string","dataMasking":[]},{"key":"reportId","name":"业务号","clientKey":"reportId","type":"fields","dataMasking":[]},{"key":"insurantName","name":"投保人","clientKey":"insurantName","type":"fields","dataMasking":[]},{"key":"insurantPhone","name":"联系电话","clientKey":"insurantPhone","type":"fields","dataMasking":[]},{"key":"agentId","key2":"fullName","name":"处理人","clientKey":"agentName","type":"twice","dataMasking":[]},{"key":"state","name":"状态","clientKey":"state","type":"state","dataMasking":[]},{"key":"ctime","name":"创建时间","clientKey":"ctime","type":"time","dataMasking":[]},{"key":"recordUploadTime","name":"上传时间","clientKey":"recordUploadTime","type":"time","dataMasking":[]},{"key":"recordDuration","name":"录制时长","clientKey":"recordDuration","type":"duration","dataMasking":[]}]
             * reportStates : [{"_id":"5e0d8eab37fc9823c43c689d","code":"untreated","name":"待录制","color":"#FFFFFF","__v":0,"backgroundColor":"#00965E"},{"_id":"5bc9b5f648496200065dfb1d","code":"checking","name":"待上传","color":"#FFFFFF","__v":0,"backgroundColor":"#FF0000"},{"_id":"5bc9b60148496200065dfb1e","code":"checked","name":"已完成","color":"#FFFFFF","__v":0,"backgroundColor":"#7F7F7F"}]
             * reportQueries : [{"key":"ctime","isFields":false,"name":"日期","placeholder":"日期","type":"rangePicker","mongoType":"Date","style":{"width":216},"allowClear":true},{"defaultKey":"5ee88000c488564bd8621344","options":[{"name":"测试租户机构坐席1","key":"5ec4eff2335c0f00221e066f"},{"name":"测试租户机构坐席2","key":"5ec4f651a4f25b0022f7763a"},{"name":"开发","key":"5edf39dd51d956456eee31d0"},{"name":"王健泉","key":"5ee88000c488564bd8621344"},{"name":"刘江","key":"5ee884c5c488564bd862134c"}],"key":"agentId","isFields":false,"name":"处理人","placeholder":"处理人","type":"select","mongoType":"ObjectId","style":{"width":216},"allowClear":true},{"key":"reportId","isFields":true,"name":"业务号","placeholder":"请输入业务号","type":"input","mongoType":"String","style":{"width":216},"allowClear":true},{"key":"insurantName","isFields":true,"name":"投保人","placeholder":"请输入投保人姓名","type":"input","mongoType":"String","style":{"width":216},"allowClear":true}]
             * showInsurance : true
             * smsContent : [{"content":"尊敬的{insurantName}，我司保险代理人邀请您远程视频办理保险业务，请您点击以下链接http://ins-online.ikandy.cn/ 进入视频。如有疑问请致电95XXX或联系您的保单服务人员。感谢您的配合，祝您生活愉快！【甜新科技】","phone":"insurantPhone","_id":"5ed5bc909614cc00182a7165"},{"content":"尊敬的{insuredName}，我司保险代理人邀请您远程视频办理保险业务，请您点击以下链接http://ins-online.ikandy.cn/ 进入视频。如有疑问请致电95XXX或联系您的保单服务人员。感谢您的配合，祝您生活愉快！【甜新科技】","phone":"insuredPhone","_id":"5ed5bc909614cc00182a7166"}]
             * status : true
             */

            private String _id;
            private String ctime;
            private String utime;
            private String creator;
            private String name;
            private String phone;
            private String bucket;
            private String showInsuranceName;
            private int transTime;
            private String validDate;
            private int maxAgents;
            private String code;
            private String remark;
            private String logo;
            private int __v;
            private String callSkill;
            private boolean showInsurance;
            private boolean status;
            private List<ReportListsAppBean> reportListsApp;
            private List<ReportListsBean> reportLists;
            private List<ReportStatesBean> reportStates;
            private List<ReportQueriesBean> reportQueries;
            private List<SmsContentBean> smsContent;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }

            public String getUtime() {
                return utime;
            }

            public void setUtime(String utime) {
                this.utime = utime;
            }

            public String getCreator() {
                return creator;
            }

            public void setCreator(String creator) {
                this.creator = creator;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getBucket() {
                return bucket;
            }

            public void setBucket(String bucket) {
                this.bucket = bucket;
            }

            public String getShowInsuranceName() {
                return showInsuranceName;
            }

            public void setShowInsuranceName(String showInsuranceName) {
                this.showInsuranceName = showInsuranceName;
            }

            public int getTransTime() {
                return transTime;
            }

            public void setTransTime(int transTime) {
                this.transTime = transTime;
            }

            public String getValidDate() {
                return validDate;
            }

            public void setValidDate(String validDate) {
                this.validDate = validDate;
            }

            public int getMaxAgents() {
                return maxAgents;
            }

            public void setMaxAgents(int maxAgents) {
                this.maxAgents = maxAgents;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public int get__v() {
                return __v;
            }

            public void set__v(int __v) {
                this.__v = __v;
            }

            public String getCallSkill() {
                return callSkill;
            }

            public void setCallSkill(String callSkill) {
                this.callSkill = callSkill;
            }

            public boolean isShowInsurance() {
                return showInsurance;
            }

            public void setShowInsurance(boolean showInsurance) {
                this.showInsurance = showInsurance;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public List<ReportListsAppBean> getReportListsApp() {
                return reportListsApp;
            }

            public void setReportListsApp(List<ReportListsAppBean> reportListsApp) {
                this.reportListsApp = reportListsApp;
            }

            public List<ReportListsBean> getReportLists() {
                return reportLists;
            }

            public void setReportLists(List<ReportListsBean> reportLists) {
                this.reportLists = reportLists;
            }

            public List<ReportStatesBean> getReportStates() {
                return reportStates;
            }

            public void setReportStates(List<ReportStatesBean> reportStates) {
                this.reportStates = reportStates;
            }

            public List<ReportQueriesBean> getReportQueries() {
                return reportQueries;
            }

            public void setReportQueries(List<ReportQueriesBean> reportQueries) {
                this.reportQueries = reportQueries;
            }

            public List<SmsContentBean> getSmsContent() {
                return smsContent;
            }

            public void setSmsContent(List<SmsContentBean> smsContent) {
                this.smsContent = smsContent;
            }

            public static class ReportListsAppBean {
                /**
                 * key : insurance
                 * key2 : name
                 * name : 产品
                 * clientKey : insuranceName
                 * type : twice
                 * dataMasking : []
                 */

                private String key;
                private String key2;
                private String name;
                private String clientKey;
                private String type;
                private List<?> dataMasking;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public String getKey2() {
                    return key2;
                }

                public void setKey2(String key2) {
                    this.key2 = key2;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getClientKey() {
                    return clientKey;
                }

                public void setClientKey(String clientKey) {
                    this.clientKey = clientKey;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public List<?> getDataMasking() {
                    return dataMasking;
                }

                public void setDataMasking(List<?> dataMasking) {
                    this.dataMasking = dataMasking;
                }
            }

            public static class ReportListsBean {
                /**
                 * key : insurance
                 * key2 : name
                 * name : 产品
                 * clientKey : insuranceName
                 * type : twice
                 * dataMasking : []
                 */

                private String key;
                private String key2;
                private String name;
                private String clientKey;
                private String type;
                private List<?> dataMasking;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public String getKey2() {
                    return key2;
                }

                public void setKey2(String key2) {
                    this.key2 = key2;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getClientKey() {
                    return clientKey;
                }

                public void setClientKey(String clientKey) {
                    this.clientKey = clientKey;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public List<?> getDataMasking() {
                    return dataMasking;
                }

                public void setDataMasking(List<?> dataMasking) {
                    this.dataMasking = dataMasking;
                }
            }

            public static class ReportStatesBean {
                /**
                 * _id : 5e0d8eab37fc9823c43c689d
                 * code : untreated
                 * name : 待录制
                 * color : #FFFFFF
                 * __v : 0
                 * backgroundColor : #00965E
                 */

                private String _id;
                private String code;
                private String name;
                private String color;
                private int __v;
                private String backgroundColor;

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public int get__v() {
                    return __v;
                }

                public void set__v(int __v) {
                    this.__v = __v;
                }

                public String getBackgroundColor() {
                    return backgroundColor;
                }

                public void setBackgroundColor(String backgroundColor) {
                    this.backgroundColor = backgroundColor;
                }
            }

            public static class ReportQueriesBean {
                /**
                 * key : ctime
                 * isFields : false
                 * name : 日期
                 * placeholder : 日期
                 * type : rangePicker
                 * mongoType : Date
                 * style : {"width":216}
                 * allowClear : true
                 * defaultKey : 5ee88000c488564bd8621344
                 * options : [{"name":"测试租户机构坐席1","key":"5ec4eff2335c0f00221e066f"},{"name":"测试租户机构坐席2","key":"5ec4f651a4f25b0022f7763a"},{"name":"开发","key":"5edf39dd51d956456eee31d0"},{"name":"王健泉","key":"5ee88000c488564bd8621344"},{"name":"刘江","key":"5ee884c5c488564bd862134c"}]
                 */

                private String key;
                private boolean isFields;
                private String name;
                private String placeholder;
                private String type;
                private String mongoType;
                private StyleBean style;
                private boolean allowClear;
                private String defaultKey;
                private List<OptionsBean> options;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public boolean isIsFields() {
                    return isFields;
                }

                public void setIsFields(boolean isFields) {
                    this.isFields = isFields;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPlaceholder() {
                    return placeholder;
                }

                public void setPlaceholder(String placeholder) {
                    this.placeholder = placeholder;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getMongoType() {
                    return mongoType;
                }

                public void setMongoType(String mongoType) {
                    this.mongoType = mongoType;
                }

                public StyleBean getStyle() {
                    return style;
                }

                public void setStyle(StyleBean style) {
                    this.style = style;
                }

                public boolean isAllowClear() {
                    return allowClear;
                }

                public void setAllowClear(boolean allowClear) {
                    this.allowClear = allowClear;
                }

                public String getDefaultKey() {
                    return defaultKey;
                }

                public void setDefaultKey(String defaultKey) {
                    this.defaultKey = defaultKey;
                }

                public List<OptionsBean> getOptions() {
                    return options;
                }

                public void setOptions(List<OptionsBean> options) {
                    this.options = options;
                }

                public static class StyleBean {
                    /**
                     * width : 216
                     */

                    private int width;

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }
                }

                public static class OptionsBean {
                    /**
                     * name : 测试租户机构坐席1
                     * key : 5ec4eff2335c0f00221e066f
                     */

                    private String name;
                    private String key;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        this.key = key;
                    }
                }
            }

            public static class SmsContentBean {
                /**
                 * content : 尊敬的{insurantName}，我司保险代理人邀请您远程视频办理保险业务，请您点击以下链接http://ins-online.ikandy.cn/ 进入视频。如有疑问请致电95XXX或联系您的保单服务人员。感谢您的配合，祝您生活愉快！【甜新科技】
                 * phone : insurantPhone
                 * _id : 5ed5bc909614cc00182a7165
                 */

                private String content;
                private String phone;
                private String _id;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }
            }
        }

        public static class OrgAccountBean {
            /**
             * _id : 5ec4ecfca4f25b0022f77608
             * createdAt : 2020-05-20T08:40:28.042Z
             * updatedAt : 2020-05-20T09:21:04.729Z
             * orgName : 测试租户--机构1
             * tenant : 5ec49d1f52efce001817b1b2
             * code : 123456
             * __v : 2
             * workingDayTime : [{"from":"10:30:00","end":"11:00:00","_id":"5ec4f680a4f25b0022f7763c"},{"from":"16:00:00","end":"18:00:00","_id":"5ec4f680a4f25b0022f7763b"}]
             * callDealing : false
             * status : true
             */

            private String _id;
            private String createdAt;
            private String updatedAt;
            private String orgName;
            private String tenant;
            private String code;
            private int __v;
            private boolean callDealing;
            private boolean status;
            private List<WorkingDayTimeBean> workingDayTime;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getOrgName() {
                return orgName;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }

            public String getTenant() {
                return tenant;
            }

            public void setTenant(String tenant) {
                this.tenant = tenant;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int get__v() {
                return __v;
            }

            public void set__v(int __v) {
                this.__v = __v;
            }

            public boolean isCallDealing() {
                return callDealing;
            }

            public void setCallDealing(boolean callDealing) {
                this.callDealing = callDealing;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public List<WorkingDayTimeBean> getWorkingDayTime() {
                return workingDayTime;
            }

            public void setWorkingDayTime(List<WorkingDayTimeBean> workingDayTime) {
                this.workingDayTime = workingDayTime;
            }

            public static class WorkingDayTimeBean {
                /**
                 * from : 10:30:00
                 * end : 11:00:00
                 * _id : 5ec4f680a4f25b0022f7763c
                 */

                private String from;
                private String end;
                private String _id;

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
                }

                public String getEnd() {
                    return end;
                }

                public void setEnd(String end) {
                    this.end = end;
                }

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }
            }
        }

        public static class DepartmentsBean {
            /**
             * _id : 5ec4f3aba4f25b0022f7762c
             * skills : ["POSA","CLAIM"]
             */

            private String _id;
            private List<String> skills;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public List<String> getSkills() {
                return skills;
            }

            public void setSkills(List<String> skills) {
                this.skills = skills;
            }
        }
    }

    public static class SysRolesBean {
        /**
         * _id : 5d5d0773d98a3e6cdd3a969f
         * roleName : 坐席
         * level : 1
         */

        private String _id;
        private String roleName;
        private String level;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }
}

