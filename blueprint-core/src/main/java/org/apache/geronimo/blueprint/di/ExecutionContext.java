/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.blueprint.di;

import java.lang.reflect.Type;
import java.util.List;

public abstract class ExecutionContext {

    private static final ThreadLocal<ExecutionContext> context = new ThreadLocal<ExecutionContext>();

    public static boolean isContextSet() {
        return context.get() != null;
    }

    public static ExecutionContext getContext() {
        ExecutionContext executionContext = context.get();
        if (executionContext == null) {
            throw new IllegalStateException("Execution container has not been set");
        }
        return executionContext;
    }

    public static ExecutionContext setContext(ExecutionContext newContext) {
        ExecutionContext oldContext = context.get();
        context.set(newContext);
        return oldContext;
    }

    /**
     * Adds a recipe to the top of the execution stack.  If the recipe is already on
     * the stack, a CircularDependencyException is thrown.
     * @param recipe the recipe to add to the stack
     * @throws CircularDependencyException if the recipe is already on the stack
     */
    public abstract void push(Recipe recipe) throws CircularDependencyException;

    /**
     * Removes the top recipe from the execution stack.
     * @return the top recipe on the stack
     */
    public abstract Recipe pop();

    /**
     * Does this context contain a object with the specified name.
     *
     * @param name the unique name of the object instance
     * @return true if this context contain a object with the specified name
     */
    public abstract boolean containsObject(String name);

    /**
     * Gets the object or recipe with the specified name from the repository.
     *
     * @param name the unique name of the object instance
     * @return the object instance, a recipe to build the object or null
     */
    public abstract Object getObject(String name);

    /**
     * Add an object to the repository.
     *
     * @param name the unique name of the object instance
     * @param object the object instance
     * @throws org.osgi.service.blueprint.container.ComponentDefinitionException if another object instance is already registered with the name
     */
    public abstract void addObject(String name, Object object, boolean partialObject);
    
    public abstract void addObject(String name, Object object);

    public abstract boolean containsCreatedObject(String name);
    
    public abstract Object getCreatedObject(String name);

    public abstract List<Recipe> getCreatedRecipes();

    public abstract Object convert(Object value, Type type) throws Exception;

    public abstract Class loadClass(String className) throws ClassNotFoundException;
}
