package org.apache.connect.mongo;

import java.util.Map;
import org.apache.connect.mongo.replicator.ReplicaSetConfig;
import org.apache.connect.mongo.replicator.ReplicaSets;
import org.junit.Assert;
import org.junit.Test;

public class ReplicaSetsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreatReplicaSetsExceptionWithoutMongoAddr() {
        ReplicaSets.create("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatReplicaSetsExceptioWithoutReplicaSetName() {
        ReplicaSets.create("127.0.0.1:27081");
    }

    @Test
    public void testCreatReplicaSetsSpecialReplicaSetName() {
        ReplicaSets replicaSets = ReplicaSets.create("replicaName1/127.0.0.1:27081,127.0.0.1:27082,127.0.0.1:27083");
        Map<String, ReplicaSetConfig> replicaSetConfigMap = replicaSets.getReplicaConfigByName();
        Assert.assertTrue(replicaSetConfigMap.size() == 1);
        Assert.assertNotNull(replicaSetConfigMap.get("replicaName1"));
        Assert.assertEquals("127.0.0.1:27081,127.0.0.1:27082,127.0.0.1:27083", replicaSetConfigMap.get("replicaName1").getHost());
        Assert.assertEquals("replicaName1", replicaSetConfigMap.get("replicaName1").getReplicaSetName());
    }

    @Test
    public void testCreatReplicaSetsSpecialShardNameAndReplicaSetName() {
        ReplicaSets replicaSets = ReplicaSets.create("shardName1=replicaName1/127.0.0.1:27081,127.0.0.1:27082,127.0.0.1:27083");
        Map<String, ReplicaSetConfig> replicaSetConfigMap = replicaSets.getReplicaConfigByName();
        Assert.assertTrue(replicaSetConfigMap.size() == 1);
        Assert.assertNotNull(replicaSetConfigMap.get("replicaName1"));
        Assert.assertEquals("127.0.0.1:27081,127.0.0.1:27082,127.0.0.1:27083", replicaSetConfigMap.get("replicaName1").getHost());
        Assert.assertEquals("replicaName1", replicaSetConfigMap.get("replicaName1").getReplicaSetName());
        Assert.assertEquals("shardName1", replicaSetConfigMap.get("replicaName1").getShardName());
    }

    @Test
    public void testCreatReplicaSetsMutiMongoAddr() {
        ReplicaSets replicaSets = ReplicaSets.create("shardName1=replicaName1/127.0.0.1:27081,127.0.0.1:27082,127.0.0.1:27083;shardName2=replicaName2/127.0.0.1:27281,127.0.0.1:27282,127.0.0.1:27283");
        Map<String, ReplicaSetConfig> replicaSetConfigMap = replicaSets.getReplicaConfigByName();
        Assert.assertTrue(replicaSetConfigMap.size() == 2);
        Assert.assertNotNull(replicaSetConfigMap.get("replicaName1"));
        Assert.assertEquals("127.0.0.1:27081,127.0.0.1:27082,127.0.0.1:27083", replicaSetConfigMap.get("replicaName1").getHost());
        Assert.assertEquals("replicaName1", replicaSetConfigMap.get("replicaName1").getReplicaSetName());
        Assert.assertEquals("shardName1", replicaSetConfigMap.get("replicaName1").getShardName());

        Assert.assertNotNull(replicaSetConfigMap.get("replicaName2"));
        Assert.assertEquals("127.0.0.1:27281,127.0.0.1:27282,127.0.0.1:27283", replicaSetConfigMap.get("replicaName2").getHost());
        Assert.assertEquals("replicaName2", replicaSetConfigMap.get("replicaName2").getReplicaSetName());
        Assert.assertEquals("shardName2", replicaSetConfigMap.get("replicaName2").getShardName());
    }

}
