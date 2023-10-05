package management.observers;

import management.events.Event;

public interface Observer
{
    void update(Event event);
}
