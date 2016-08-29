/**
 * Copyright (c) 2014-2016, zbore technology. All rights reserved.
*/

package com.zbore.suite.sample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.zbore.suite.sample.callback.IDisplay;

/**
 * @ClassName: ModelWrapper
 *
 * @author liujun
 * @date 2015-8-18
 */
public class ModelWrapper {

    public static class MHuxin implements IDisplay {
        public  String name;

        public MHuxin(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public static class MArea implements  IDisplay {

        /**
         * 名字
         */
        public String name;

        /**
         * 起始面积
         */
        public int from;

        /**
         * 结束面积
         */
        public int to;

        public MArea(String name, int from, int to) {
            this.name = name;
            this.from = from;
            this.to = to;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public static class MusicItem {
        public String title;
        public String url;

        public MusicItem(String title, String url) {
            this.title = title;
            this.url = url;
        }
    }

    public static class LocalFile implements Parcelable {
        public String uri; // uri
        public String path; // 本地绝对路径
        public String name; // 文件名
        public String folderName; // 文件夹名

        public LocalFile() {
            super();
        }

        public LocalFile(String uri, String path, String name, String folderName) {
            super();
            this.uri = uri;
            this.path = path;
            this.name = name;
            this.folderName = folderName;
        }

        @Override
        public String toString() {
            return uri;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(uri);
            dest.writeString(path);
            dest.writeString(name);
            dest.writeString(folderName);
        }
        
        private LocalFile(Parcel source) {
            uri = source.readString();
            path = source.readString();
            name = source.readString();
            folderName = source.readString();
        }

        public static final Creator<LocalFile> CREATOR = new Creator<LocalFile>() {

            @Override
            public LocalFile createFromParcel(Parcel source) {
                return new LocalFile(source);
            }

            @Override
            public LocalFile[] newArray(int size) {
                return new LocalFile[size];
            }
        };
    }
}
