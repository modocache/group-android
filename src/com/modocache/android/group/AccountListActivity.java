package com.modocache.android.group;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AccountListActivity extends ListActivity {
    public static String intentAccountKey = "account";
    protected AccountManager accountManager;
    protected Intent intent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        
        accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Log.w("ACCOUNTS", Integer.toString(accounts.length));
        ArrayAdapter<Account> arrayAdapter =
                new ArrayAdapter<Account>(this,
                                          android.R.layout.simple_list_item_1,
                                          accounts);
        this.setListAdapter(arrayAdapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Account account = (Account) getListView().getItemAtPosition(position);
        Intent intent = new Intent(this, AppInfoActivity.class);
        intent.putExtra(intentAccountKey, account);
        startActivity(intent);
    }
}
