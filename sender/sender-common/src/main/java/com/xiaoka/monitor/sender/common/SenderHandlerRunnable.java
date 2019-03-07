package com.xiaoka.monitor.sender.common;


import com.google.common.collect.Maps;
import com.ustcinfo.ishare.eip.si.cache.common.ICache;
import com.xiaoka.monitor.cache.AlarmEventCache;
import com.xiaoka.monitor.cache.ReceiverCache;
import com.xiaoka.monitor.cache.SenderScriptCache;
import com.xiaoka.monitor.cache.TeamCache;
import com.xiaoka.monitor.constant.AlarmSenderType;
import com.xiaoka.monitor.store.common.AlarmLog;
import com.xiaoka.monitor.store.common.IAlarmLogService;
import com.xiaoka.monitor.utils.SpringContextUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 负责发送告警的任务
 */
public class SenderHandlerRunnable implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 缓存的告警发送脚本class
     */
    private static Class QQ_SENDER_CLASS = null;
    private static Class WX_SENDER_CLASS = null;
    private static Class EMAIL_SENDER_CLASS = null;
    private static Class DING_DING_SENDER_CLASS = null;
    private static Class SMS_SENDER_CLASS = null;
    private static Class HTTP_SENDER_CLASS = null;
    /**
     * 缓存的告警发送脚本
     */
    private static String QQ_SENDER_SCRIPT = null;
    private static String WX_SENDER_SCRIPT = null;
    private static String EMAIL_SENDER_SCRIPT = null;
    private static String DING_DING_SENDER_SCRIPT = null;
    private static String SMS_SENDER_SCRIPT = null;
    private static String HTTP_SENDER_SCRIPT = null;
    /**
     * 需要发送的告警事件
     */
    private AlarmEventCache alarmEvent;

    public SenderHandlerRunnable(AlarmEventCache alarmEventCache) {
        this.alarmEvent = alarmEventCache;
    }

    @Override
    public void run() {
        /**
         * 获取接收者或发送者
         */
        ICache cache = SpringContextUtils.getBean(ICache.class);
        String receiverId = alarmEvent.getReceiverId();
        ReceiverCache receiver = cache.get(ReceiverCache.class, receiverId);
        if (receiver == null) {
            logger.error("{} 关联的接收者找不到，告警发送失败!");
            return;
        }
        String sendType = receiver.getSendType();
        Class senderClass = null;
        /**
         * 方便后面使用
         */
        List<SenderScriptCache> scriptList = cache.getAll(SenderScriptCache.class);
        Map<String, SenderScriptCache> scriptMap = Maps.newHashMap();
        for (SenderScriptCache script : scriptList) {
            scriptMap.put(script.getSendType(), script);
        }
        Class<?> runClass = null;
        switch (sendType) {
            case AlarmSenderType.QQ:
                SenderScriptCache qqScript = scriptMap.get(sendType);
                if (QQ_SENDER_SCRIPT == null || !QQ_SENDER_SCRIPT.equals(qqScript.getScript())) {
                    QQ_SENDER_CLASS = createClass(qqScript.getScript());
                    QQ_SENDER_SCRIPT = qqScript.getScript();
                }
                runClass = QQ_SENDER_CLASS;
                break;
            case AlarmSenderType.WX:
                SenderScriptCache wxScript = scriptMap.get(sendType);
                if (WX_SENDER_SCRIPT == null || !WX_SENDER_SCRIPT.equals(wxScript.getScript())) {
                    WX_SENDER_CLASS = createClass(wxScript.getScript());
                    WX_SENDER_SCRIPT = wxScript.getScript();
                }
                runClass = WX_SENDER_CLASS;
                break;
            case AlarmSenderType.DING_DING:
                SenderScriptCache dingScript = scriptMap.get(sendType);
                if (DING_DING_SENDER_SCRIPT == null || !DING_DING_SENDER_SCRIPT.equals(dingScript.getScript())) {
                    DING_DING_SENDER_CLASS = createClass(dingScript.getScript());
                    DING_DING_SENDER_SCRIPT = dingScript.getScript();
                }
                runClass = DING_DING_SENDER_CLASS;
                break;
            case AlarmSenderType.EMAIL:
                SenderScriptCache emailScript = scriptMap.get(sendType);
                if (EMAIL_SENDER_SCRIPT == null || !EMAIL_SENDER_SCRIPT.equals(emailScript.getScript())) {
                    EMAIL_SENDER_CLASS = createClass(emailScript.getScript());
                    EMAIL_SENDER_SCRIPT = emailScript.getScript();
                }
                runClass = EMAIL_SENDER_CLASS;
                break;
            case AlarmSenderType.SMS:
                SenderScriptCache smsScript = scriptMap.get(sendType);
                if (SMS_SENDER_SCRIPT == null || !SMS_SENDER_SCRIPT.equals(smsScript.getScript())) {
                    SMS_SENDER_CLASS = createClass(smsScript.getScript());
                    SMS_SENDER_SCRIPT = smsScript.getScript();
                }
                runClass = SMS_SENDER_CLASS;
                break;
            case AlarmSenderType.HTTP:
                SenderScriptCache httpScript = scriptMap.get(sendType);
                if (HTTP_SENDER_SCRIPT == null || !HTTP_SENDER_SCRIPT.equals(httpScript.getScript())) {
                    HTTP_SENDER_CLASS = createClass(httpScript.getScript());
                    HTTP_SENDER_SCRIPT = httpScript.getScript();
                }
                runClass = HTTP_SENDER_CLASS;
                break;
            default:
                logger.error("不支持{}类型告警发送");
        }
        if (runClass != null) {
            AlarmLog alarmLog = new AlarmLog();
            try {
                Binding bind = new Binding();
                /**
                 * 获取所有团队成员信息
                 */
                TeamCache team = cache.get(TeamCache.class, alarmEvent.getTeamId());
                bind.setVariable("team", team);
                bind.setVariable("receiver", receiver);
                bind.setVariable("alarmEvent", alarmEvent);
                bind.setVariable("alarmLog", alarmLog);
                Script script = InvokerHelper.createScript(runClass, bind);
                script.run();
            } catch (Exception e) {
                e.printStackTrace();
                alarmLog.setErrorStack(e.getMessage());
            }
            IAlarmLogService alarmLogService = SpringContextUtils.getBean(IAlarmLogService.class);
            // 持久化告警日志
            alarmLogService.save(alarmLog);
        }
    }

    /**
     * 根据脚本生成Class
     *
     * @param script
     * @return
     */
    private Class<?> createClass(String script) {
        if (!StringUtils.isNotBlank(script)) {
            logger.error("脚本为空，生成Class失败");
            return null;
        }
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        GroovyClassLoader loader = null;
        try {
            loader = new GroovyClassLoader(parent);
            return loader.parseClass(script);
        } finally {
            if (loader != null) {
                try {
                    loader.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
