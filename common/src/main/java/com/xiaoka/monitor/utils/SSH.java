package com.xiaoka.monitor.utils;

import ch.ethz.ssh2.Connection;
import com.google.common.collect.Lists;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.xiaoka.monitor.constant.ResponseCode;
import com.xiaoka.monitor.vo.Resp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * ssh操作工具类
 */
public class SSH {

    private static Logger logger = LoggerFactory.getLogger(SSH.class);

    /**
     * 主机是否可以ping通
     * 测试网络连接情况
     *
     * @param ip       被测试主机IP
     * @param port     ssh连接端口、默认22
     * @param username ssh连接用户名
     * @param password ssh连接用户对应的密码
     * @return
     */
    public static Resp ping(String ip, Integer port, String username, String password) {
        Resp resp = new Resp(ResponseCode.NO_AUTH);
        try {
            Callable<Resp> callable = new Callable<Resp>() {
                @Override
                public Resp call() throws Exception {
                    Connection connection = null;
                    try {
                        connection = new Connection(ip, port);
                        connection.connect(null, 10000, 10000);
                        boolean ok = connection.authenticateWithPassword(username, password);
                        if (ok) {
                            resp.setCode(ResponseCode.OK);
                        }
                        return resp;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            connection.close();
                        }
                    }
                    return resp;
                }
            };
            FutureTask<Resp> pingTask = new FutureTask<Resp>(callable);
            new Thread(pingTask).start();
            return pingTask.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }


    /**
     * 远程执行执行命令
     *
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param cmdList
     * @return List<String> 命令行结果
     */
    public static Resp exeCmd(String ip, Integer port, String username, String password, List<String> cmdList) {
        Resp resp = new Resp(ResponseCode.NO_AUTH);
        List<String> responseList = Lists.newArrayList();
        try {
            Callable<Resp> callable = new Callable<Resp>() {
                @Override
                public Resp call() throws Exception {
                    Session session = null;
                    Channel channel = null;
                    final Integer timeOut = 60000;
                    try {
                        JSch jsch = new JSch();
                        session = jsch.getSession(username, ip, port == null ? 22 : port);
                        if (StringUtils.isNotBlank(password)) {
                            session.setPassword(password);
                        }
                        session.setConfig("StrictHostKeyChecking", "no");
                        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
                        session.setServerAliveInterval(timeOut);
                        session.connect(timeOut);
                        channel = session.openChannel("shell");
                        ((ChannelShell) channel).setAgentForwarding(true);
                        ((ChannelShell) channel).setPtyType("xterm");
                        InputStream inStream = channel.getInputStream();
                        OutputStream inputToChannel = channel.getOutputStream();
                        PrintStream commander = new PrintStream(inputToChannel, true);
                        channel.connect();
                        if (cmdList != null) {
                            for (String command : cmdList) {
                                /** 输入命令 */
                                commander.print(command);
                                /** 模拟回车 */
                                commander.write(new byte[]{(byte) 0x0d});
                            }
                        }
                        /** 退出控制台 */
                        commander.print("exit");
                        commander.write(new byte[]{(byte) 0x0d});
                        BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            responseList.add(line);
                        }
                        resp.setCode(ResponseCode.OK);
                        resp.setData(responseList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("执行命令:{} 失败", cmdList);
                    } finally {
                        if (session != null) {
                            session.disconnect();
                        }
                        if (channel != null) {
                            channel.disconnect();
                        }
                    }
                    return resp;
                }
            };
            FutureTask<Resp> pingTask = new FutureTask<Resp>(callable);
            new Thread(pingTask).start();
            return pingTask.get(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * 获取服务器默认的python绝对路径
     *
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static String getPythonPath(String ip, Integer port, String username, String password) {
        String pythonPath = null;
        List<String> cmdList = Lists.newArrayList();
        cmdList.add("which python");
        Resp resp = exeCmd(ip, port, username, password, cmdList);
        if (resp.ok()) {
            if (resp.getData() == null) {
                return pythonPath;
            }
            List<String> lineList = (List<String>) resp.getData();
            if (lineList == null) {
                return pythonPath;
            }
            for (String line : lineList) {
                if (StringUtils.isNotBlank(line) && line.contains("python")) {
                    pythonPath = line.trim();
                }
            }
        }
        return pythonPath;
    }

    /**
     * 获取python版本
     *
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static String getPythonVersion(String ip, Integer port, String username, String password) {
        String pythonPath = getPythonPath(ip, port, username, password);
        if (StringUtils.isEmpty(pythonPath)) {
            return "";
        }
        String pythonVersion = "";
        List<String> cmdList = Lists.newArrayList();
        cmdList.add(pythonPath + " -V");
        Resp resp = exeCmd(ip, port, username, password, cmdList);
        if (resp.ok()) {
            if (resp.getData() == null) {
                return pythonVersion;
            }
            List<String> lineList = (List<String>) resp.getData();
            if (lineList == null) {
                return pythonVersion;
            }
            for (String line : lineList) {
                if (StringUtils.isNotBlank(line) && line.contains("Python")) {
                    pythonVersion = line.trim();
                }
            }
        }
        return pythonVersion;
    }


    public static void main(String[] args) {
        String ip = "192.168.80.98";
        String username = "open";
        String password = "open";
        //System.out.println(getPythonPath(ip, 22, username, password));
        System.out.println(getPythonVersion(ip, 22, username, password));
    }

}
