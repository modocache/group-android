package com.modocache.android.group.api;

public class GroupPermissions {
    public static enum PermissionType {
        UPDATE, DESTROY;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        };
    }
}
