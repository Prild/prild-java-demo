package com.thank.activiti.interceptor;

import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;

/**
 * MDC打印信息的拦截器
 */
public class MDCCommandInvoker extends DebugCommandInvoker {


    /**
     * 参考{@link DebugCommandInvoker}
     * @param runnable
     */
    @Override
    public void executeOperation(Runnable runnable) {
        // 不管LogMDC是否生效, 都先弄生效
        LogMDC.setMDCEnabled(true);
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation)runnable;
            if (operation.getExecution() != null) {
                // 如果是一个可操作的对象, 才放入MDC上下文中
                LogMDC.putMDCExecution(operation.getExecution());
            }
        }

        super.executeOperation(runnable);

        // 清空一下
        LogMDC.clear();
        // 如果LogMDC是不生效的, 就还原一下
        if (!LogMDC.isMDCEnabled()) {
            LogMDC.setMDCEnabled(false);
        }
    }
}
