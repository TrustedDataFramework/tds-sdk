## tds sdk

dependencies:

https://scans.gradle.com/s/22zpxxnvn343c/dependencies?focusedDependencyView=details&toggled=W1swXSxbMCwxXV0

The local jar is in release。

``````java
Class.forName("org.sdk.util.Init")
```
+ Keystore
    + 生成keystore
    ```java
    KeystoreUtility.generateKeyStore(String password)
    参数：password:密码
    返回值：KeyStoreImpl（Object）
    ```
    + 解析keystore，获取私钥
    ```java
    KeystoreUtility.decryptKeyStore(KeyStoreImpl keyStore, String password)
    参数：keyStore
         password:密码
    返回值：私钥（String）
    ```
    + 私钥转公钥
    ```java
    KeystoreUtility.privateKeyToPublicKey(String privateKey)
    参数：privateKey:私钥
    返回值：公钥（String）
    ```
    + 公钥转地址
    ```java
    KeystoreUtility.publicKeyToAddress(String publicKey)
    参数：publicKey:公钥
    返回值：地址（String）
    ```
    
+ Transaction
    + 保存、更新用户
    ```java
    TransactionUtility.saveUser(long nonce, HexBytes privateKey, String address, String username, 
                                int role, String org, int orgType)
    参数： nonce:序号
          privateKey:私钥
          address:地址
          username:用户名,
          role:用户角色 0.root 超级管理员 1.admin 管理员 2.user 普通用户
          org:组织名称
          orgType:组织类型 0.factory 1.training institution 2.testing organization:组织类型 3.other
    返回值：Transaction（Object）
    ```
    + 删除用户
    ```java
    TransactionUtility.deleteUser(long nonce, HexBytes privateKey, String address)
    参数： nonce:序号
          privateKey:私钥
          address:地址
    返回值：Transaction（Object）
    ```
    + 更改超级管理员
    ```java
    TransactionUtility.changeOwnerUser(long nonce, HexBytes privateKey, String address)
    参数： nonce:序号
          privateKey:私钥
          address:地址
    返回值：Transaction（Object）
    ```
    + 保存、更新WPQR流程
    ```java
    TransactionUtility.saveWeld(long nonce, HexBytes privateKey, String wpqr, HexBytes groupHash, int type, Accessory accessory)
    参数： nonce:序号
          privateKey:私钥
          wpqr:wpqr编号
          groupHash:第一个字节为0-6，代表类型，后面为文件哈希。
          type:0.wdld 1.accessory
          accessory:int type(流程序号)，String accessoryName(附件名称)，HexBytes accessoryHash(附件哈希)
    返回值：Transaction（Object）
    
    *注:type=0时，accessory 传 null。type=1时，groupHash传null。
    ```
    + 更新附件
    ```java
    TransactionUtility.updateAccessory(long nonce, HexBytes privateKey, String wpqr, int type, String accessoryName, HexBytes accessoryHash)
    参数： nonce:序号
          privateKey:私钥
          wpqr:wpqr编号
          groupHash:第一个字节为0-6，代表类型，后面为文件哈希。
          type:类型1.WPQR详情 3.焊接实验详情 4.实验结果 5.理化报告(非破坏性)
          accessoryName:附件名称
          accessoryHash:附件哈希
    返回值：Transaction（Object）
 
