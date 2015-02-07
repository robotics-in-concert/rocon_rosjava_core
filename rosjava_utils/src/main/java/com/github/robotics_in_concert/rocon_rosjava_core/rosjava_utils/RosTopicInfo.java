package com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils;

/*****************************************************************************
** Imports
*****************************************************************************/

import org.ros.exception.RosRuntimeException;
import org.ros.master.client.MasterStateClient;
import org.ros.master.client.TopicType;
import org.ros.node.Node;

import java.util.ArrayList;
import java.util.List;

/*****************************************************************************
** Classes
*****************************************************************************/

public class RosTopicInfo {
	private MasterStateClient master;

	public RosTopicInfo(Node caller) {
		this.master = new MasterStateClient(caller, caller.getMasterUri());
	}
	
	/**
	 * Get all topics of the specified type and return a list of topic names.
	 * 
	 * @param type : the topic type to search with (e.g. std_msgs/String)
	 * @param timeout : how long to wait for a result
	 * @return list of the topic names.
	 * 
	 * @raise RosRuntimeException if the operation times out.
	 */
	public List<String> findTopics(String type, double timeout) {
		double duration = 0.0;
		double period = 0.1;
		List<TopicType> topics = new ArrayList<TopicType>();
		List<String> topic_names = new ArrayList<String>();
		while (true) {
			topics = this.master.getTopicTypes();
			for (TopicType topic : topics) {
				// System.out.println("Topic type: " + topic_type.getName() + " : " + topic_type.getMessageType());
				if (topic.getMessageType().equals(type)) {
					topic_names.add(topic.getName());
				}
			}
			if ( topic_names.size() > 0 ) {
				break;
			}
            try {
                Thread.sleep((int)(1000*period));
            } catch (Exception e) {
                throw new RosRuntimeException(e);
            }
            duration += 1000*period;
            if ( duration > timeout ) {
            	throw new RosRuntimeException("timed out looking for topics of type [" + type + "]");
            }
		}
		return topic_names;
	}
	
	/**
	 * Look for a unique topic of the type specified. 
	 * 
	 * @param type : the topic type to search with (e.g. std_msgs/String)
	 * @return the topic name
	 * 
	 * @todo a timeout dependent variation of this - currently hardcoded to 15s.
	 * 
	 * @raise RosRuntimeException if the operation times out.
	 */
	public String findTopic(String type) {
		List<String> topic_names = this.findTopics(type, 15.0);
		if ( topic_names.size() != 1 ) {
			// bad choice of exception
			throw new RosRuntimeException("couldn't find a unique topic of type  [" + type + "]");
		}
		return topic_names.get(0);
	}

	// Rosjava master client currently doesn't find any information about services yet.
//	String findService(String type, double timeout) {
//		String name = "";
//		return name;
//	}
//
//	String findUniqueService(String type) {
//		double timeout = 15.0;
//		String name = "";
//		return name;
//	}
}
