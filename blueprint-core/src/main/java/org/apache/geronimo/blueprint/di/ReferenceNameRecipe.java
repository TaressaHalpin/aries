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

import java.util.Collections;
import java.util.List;

import org.osgi.service.blueprint.container.ComponentDefinitionException;
import org.osgi.service.blueprint.container.NoSuchComponentException;

/*
 * The ReferenceNameRecipe is used to inject the reference name into the object (as a String).
 * The ReferenceNameRecipe ensures the actual reference object exists before the reference name is injected. 
 */
public class ReferenceNameRecipe extends AbstractRecipe {
    
    private String referenceName;

    public ReferenceNameRecipe(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getReferenceName() {
        return referenceName;
    }

    private Object getReference() {
        if (referenceName == null) {
            throw new ComponentDefinitionException("Reference name has not been set");
        }
        ExecutionContext context = ExecutionContext.getContext();
        if (!context.containsObject(referenceName)) {
            throw new NoSuchComponentException(referenceName);
        }
        return context.getObject(referenceName);
    }
    
    public List<Recipe> getNestedRecipes() {
        Object object = getReference();
        if (object instanceof Recipe) {
            Recipe recipe = (Recipe) object;
            return Collections.singletonList(recipe);
        } else {
            return Collections.emptyList();
        }
    }

    protected Object internalCreate() throws ComponentDefinitionException {
        Object object = getReference();
        return referenceName;
    }
    
}
