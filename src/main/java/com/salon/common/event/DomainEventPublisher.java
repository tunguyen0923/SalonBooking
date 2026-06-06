package com.salon.common.event;

public interface DomainEventPublisher {
    void publish(Object event);
}
