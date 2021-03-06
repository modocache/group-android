package com.modocache.android.group;

import java.util.ArrayList;

import com.modocache.android.group.api.GroupAPIAuthenticationCallback;
import com.modocache.android.group.api.GroupAPIEngine;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class GetGoogleAppsAccountActivity extends Activity {
    public static String intentAccountKey = "account";
    private static int addAccountRequestCode = 1;
    private static Intent addAccountIntent = null;

    @Override
    protected void onResume() {
        super.onResume();
        String domainSuffix = getString(R.string.app_domain);
        promptForAccountWithSuffix(domainSuffix);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == addAccountRequestCode) {
            addAccountIntent = null;
        }
    }

    private Account getGoogleAppsAccount(String domainSuffix) {
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");
        ArrayList<Account> validAccounts = new ArrayList<Account>();
        for (Account account : accounts) {
            if (account.name.endsWith(domainSuffix)) {
                validAccounts.add(account);
            }
        }

        if (validAccounts.size() == 1) {
            return validAccounts.get(0);
        } else if (validAccounts.isEmpty()) {
            return null;
        } else {
            // TODO: Use getAccountsByType to prompt for and return selection
            return null;
        }
    }

    private void promptForAccountWithSuffix(String domainSuffix) {
        Account appsAccount = getGoogleAppsAccount(domainSuffix);
        if (appsAccount != null) {
            GroupAPIEngine.getSharedEngine().authenticateAccount(this, appsAccount, new GroupAPIAuthenticationCallback() {
                @Override
                public void onAuthenticationComplete(Boolean result) {
                    Intent intent = new Intent(getBaseContext(), GroupMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        } else if (addAccountIntent == null) {
            String toastText = String.format(getString(R.string.add_account_toast),
                                             domainSuffix);
            Toast.makeText(getBaseContext(), toastText, Toast.LENGTH_LONG).show();
            addAccountIntent = new Intent(android.provider.Settings.ACTION_ADD_ACCOUNT);
            startActivityForResult(addAccountIntent, addAccountRequestCode);
        }
    }
}