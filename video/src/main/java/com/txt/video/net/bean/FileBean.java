package com.txt.video.net.bean;



import com.txt.video.common.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/27.
 * description：
 */
public  class FileBean implements MultiItemEntity {

    /**
     * list : [{"_id":"5f475da073bea64f44efc7ac","ctime":"2020-08-27T07:15:44.993Z","utime":"2020-08-27T07:15:44.993Z","name":"IMG_20190805_150633.jpg","uploadAgent":{"_id":"5f475c759d0d164be1f47773","fullName":"wjqdev"},"updateAgent":{"_id":"5f475c759d0d164be1f47773","fullName":"wjqdev"},"tenant":"5f437b28708bd45de0d6452b","orgAccount":"5f437b4a708bd45de0d6452c","isPublic":false,"url":"https://s3.cn-north-1.amazonaws.com.cn/gdrb-dingsun-test/wjqdev/1598512544852.*","__v":0},{"_id":"5f44b776332119557402bc35","ctime":"2020-08-25T07:02:14.591Z","utime":"2020-08-25T07:02:14.591Z","name":"车型.txt","uploadAgent":{"_id":"5f437bc3708bd45de0d6452e","fullName":"测试管理员"},"updateAgent":{"_id":"5f437bc3708bd45de0d6452e","fullName":"测试管理员"},"tenant":"5f437b28708bd45de0d6452b","orgAccount":"5f437b4a708bd45de0d6452c","isPublic":true,"url":"https://s3.cn-north-1.amazonaws.com.cn/gdrb-dingsun-test/5f437b28708bd45de0d6452b/1598338934531","__v":0}]
     * pageIndex : 1
     * pageSize : 10
     * count : 2
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

    @Override
    public int getItemType() {
        return 1;
    }


    public static class ListBean implements MultiItemEntity {
        /**
         * idpath : .0.29226.29227.
         * idpathName : 智慧展业
         */
        private boolean isUploading;

        public boolean isUploading() {
            return isUploading;
        }

        public void setUploading(boolean uploading) {
            isUploading = uploading;
        }

        private String idpath;
        private String idpathName;

        /**
         * _id : 5f475da073bea64f44efc7ac
         * ctime : 2020-08-27T07:15:44.993Z
         * utime : 2020-08-27T07:15:44.993Z
         * name : IMG_20190805_150633.jpg
         * uploadAgent : {"_id":"5f475c759d0d164be1f47773","fullName":"wjqdev"}
         * updateAgent : {"_id":"5f475c759d0d164be1f47773","fullName":"wjqdev"}
         * tenant : 5f437b28708bd45de0d6452b
         * orgAccount : 5f437b4a708bd45de0d6452c
         * isPublic : false
         * url : https://s3.cn-north-1.amazonaws.com.cn/gdrb-dingsun-test/wjqdev/1598512544852.*
         * __v : 0
         */

//        @Nullable
//        @Override
//        public List<BaseNode> getChildNode() {
//            return null;
//        }


        @Override
        public int getItemType() {
            return 1;
        }

        private String _id;
        private String ctime;
        private String utime;
        private String name;
        private UploadAgentBean uploadAgent;
        private UpdateAgentBean updateAgent;
        private String tenant;
        private String orgAccount;
        private boolean isPublic;
        private String url;
        private int __v;
        private List<String> images;

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

        public UploadAgentBean getUploadAgent() {
            return uploadAgent;
        }

        public void setUploadAgent(UploadAgentBean uploadAgent) {
            this.uploadAgent = uploadAgent;
        }

        public UpdateAgentBean getUpdateAgent() {
            return updateAgent;
        }

        public void setUpdateAgent(UpdateAgentBean updateAgent) {
            this.updateAgent = updateAgent;
        }

        public String getTenant() {
            return tenant;
        }

        public void setTenant(String tenant) {
            this.tenant = tenant;
        }

        public String getOrgAccount() {
            return orgAccount;
        }

        public void setOrgAccount(String orgAccount) {
            this.orgAccount = orgAccount;
        }

        public boolean isIsPublic() {
            return isPublic;
        }

        public void setIsPublic(boolean isPublic) {
            this.isPublic = isPublic;
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

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getIdpath() {
            return idpath;
        }

        public void setIdpath(String idpath) {
            this.idpath = idpath;
        }

        public String getIdpathName() {
            return idpathName;
        }

        public void setIdpathName(String idpathName) {
            this.idpathName = idpathName;
        }

        public static class UploadAgentBean {
            /**
             * _id : 5f475c759d0d164be1f47773
             * fullName : wjqdev
             */

            private String _id;
            private String fullName;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }
        }

        public static class UpdateAgentBean {
            /**
             * _id : 5f475c759d0d164be1f47773
             * fullName : wjqdev
             */

            private String _id;
            private String fullName;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }
        }
    }
}
