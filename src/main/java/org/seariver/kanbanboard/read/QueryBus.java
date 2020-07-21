package org.seariver.kanbanboard.read;

import org.seariver.kanbanboard.read.domain.application.Query;
import org.seariver.kanbanboard.read.domain.application.Resolver;
import org.seariver.kanbanboard.read.observable.QueryEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class QueryBus {

    private ApplicationContext context;
    private ApplicationEventPublisher publisher;

    public QueryBus(ApplicationContext context, ApplicationEventPublisher publisher) {
        this.context = context;
        this.publisher = publisher;
    }

    public void execute(Query query) {

        var event = new QueryEvent(query);

        try {
            resolve(query);
        } catch (Exception exception) {
            event.setException(exception);
            throw exception;
        } finally {
            event.stopTimer();
            publisher.publishEvent(event);
        }
    }

    private void resolve(Query query) {
        var resolverName = query.getClass().getSimpleName().replace("Query", "Resolver");
        var resolverBeanName = Character.toLowerCase(resolverName.charAt(0)) + resolverName.substring(1);
        Resolver<Query> resolver = (Resolver) context.getBean(resolverBeanName);
        resolver.resolve(query);
    }
}
