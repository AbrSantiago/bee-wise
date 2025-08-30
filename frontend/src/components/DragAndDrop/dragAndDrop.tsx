import React, { useState } from 'react';
import { DndContext, DragEndEvent } from '@dnd-kit/core';
import { Draggable } from './Draggable';
import { Droppable } from './Droppable';

function Example() {
  const [parent, setParent] = useState<string | null>(null);

  const draggable = (
    <Draggable id="draggable">
      Go ahead, drag me.
    </Draggable>
  );

  function handleDragEnd(event: DragEndEvent) {
    setParent(event.over ? event.over.id : null);
  }

  return (
    <DndContext onDragEnd={handleDragEnd}>
      {!parent ? draggable : null}
      <Droppable id="droppable">
        {parent === "droppable" ? draggable : 'Drop here'}
      </Droppable>
    </DndContext>
  );
}

export default Example;