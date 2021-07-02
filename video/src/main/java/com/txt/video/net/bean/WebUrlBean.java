package com.txt.video.net.bean;

import java.util.List;

/**
 * author ：Justin
 * time ：4/12/21.
 * des ：
 */
public class WebUrlBean {

    /**
     * list : [{"_id":"6073c16d55ccc4594220239b","ctime":"2021-04-12T03:41:33.453Z","utime":"2021-04-12T03:41:33.453Z","name":"test","url":"https://recall-sync-demo.cloud-ins.cn/","__v":0,"agentInfo":{"uploadAgentId":"6073aa14b3057900185be685","uploadAgentLoginName":"wjq123","uploadAgentFullName":"wang","tenant":"6073a66485f09f7df00b82f9","tenantName":"甜新展业测试","orgAccount":"6073a71709e5c300183705f4","orgAccountName":"甜新展业测试机构"}}]
     * pageIndex : 1
     * pageSize : 10
     * count : 1
     */

    private int pageIndex;
    private int pageSize;
    private int count;
    private List<ListBean> list;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * _id : 6073c16d55ccc4594220239b
         * ctime : 2021-04-12T03:41:33.453Z
         * utime : 2021-04-12T03:41:33.453Z
         * name : test
         * url : https://recall-sync-demo.cloud-ins.cn/
         * __v : 0
         * agentInfo : {"uploadAgentId":"6073aa14b3057900185be685","uploadAgentLoginName":"wjq123","uploadAgentFullName":"wang","tenant":"6073a66485f09f7df00b82f9","tenantName":"甜新展业测试","orgAccount":"6073a71709e5c300183705f4","orgAccountName":"甜新展业测试机构"}
         */

        private String _id;
        private String ctime;
        private String utime;
        private String name;
        private String url;
        private int __v;
        private AgentInfoBean agentInfo;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public AgentInfoBean getAgentInfo() {
            return agentInfo;
        }

        public void setAgentInfo(AgentInfoBean agentInfo) {
            this.agentInfo = agentInfo;
        }

        public static class AgentInfoBean {
            /**
             * uploadAgentId : 6073aa14b3057900185be685
             * uploadAgentLoginName : wjq123
             * uploadAgentFullName : wang
             * tenant : 6073a66485f09f7df00b82f9
             * tenantName : 甜新展业测试
             * orgAccount : 6073a71709e5c300183705f4
             * orgAccountName : 甜新展业测试机构
             */

            private String uploadAgentId;
            private String uploadAgentLoginName;
            private String uploadAgentFullName;
            private String tenant;
            private String tenantName;
            private String orgAccount;
            private String orgAccountName;

            public String getUploadAgentId() {
                return uploadAgentId;
            }

            public void setUploadAgentId(String uploadAgentId) {
                this.uploadAgentId = uploadAgentId;
            }

            public String getUploadAgentLoginName() {
                return uploadAgentLoginName;
            }

            public void setUploadAgentLoginName(String uploadAgentLoginName) {
                this.uploadAgentLoginName = uploadAgentLoginName;
            }

            public String getUploadAgentFullName() {
                return uploadAgentFullName;
            }

            public void setUploadAgentFullName(String uploadAgentFullName) {
                this.uploadAgentFullName = uploadAgentFullName;
            }

            public String getTenant() {
                return tenant;
            }

            public void setTenant(String tenant) {
                this.tenant = tenant;
            }

            public String getTenantName() {
                return tenantName;
            }

            public void setTenantName(String tenantName) {
                this.tenantName = tenantName;
            }

            public String getOrgAccount() {
                return orgAccount;
            }

            public void setOrgAccount(String orgAccount) {
                this.orgAccount = orgAccount;
            }

            public String getOrgAccountName() {
                return orgAccountName;
            }

            public void setOrgAccountName(String orgAccountName) {
                this.orgAccountName = orgAccountName;
            }
        }
    }
}
