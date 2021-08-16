[toc]

## JVM

### JVM 内存模型

1.　xmx <= 物理内存的70%

### JVM 命令行工具

1. jps -lmv

2. jinfo {pid}

3. jstat -gc 91739 1000 1000

4. jstat -gcutil 91739 1000 1000 （百分比）

5. jmap -histo 91739

6. jstack -l 91739　线程信息

7. jcmd

	> (env) wangtaodeMac-mini:code wangtao$ jcmd 91739 help
	> 91739:
	> The following commands are available:
	> JFR.stop
	> JFR.start
	> JFR.dump
	> JFR.check
	> VM.native_memory
	> VM.check_commercial_features
	> VM.unlock_commercial_features
	> ManagementAgent.stop
	> ManagementAgent.start_local
	> ManagementAgent.start
	> VM.classloader_stats
	> GC.rotate_log
	> Thread.print
	> GC.class_stats
	> GC.class_histogram
	> GC.heap_dump
	> GC.finalizer_info
	> GC.heap_info
	> GC.run_finalization
	> GC.run
	> VM.uptime
	> VM.dynlibs
	> VM.flags
	> VM.system_properties
	> VM.command_line
	> VM.version
	> help

### 图形化工具

1.　jconsole
2.　jvisualvm
3.　VisualGC
4.　jmc

### GC 背景＆原理

1. Y-gen
	- Eden/S0/S1 = 8/1/1