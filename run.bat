java -Xms128M -Xmx3000m -XX:PermSize=64M -XX:MaxPermSize=256M -Dderby.storage.pageCacheSize=10000 -Dderby.storage.pageSize=32768 -jar myjaphoo.jar  %*
