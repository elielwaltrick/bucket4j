/*
 *
 *   Copyright 2015-2017 Vladimir Bukhtoyarov
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.bucket4j.util;

import java.util.function.Supplier;

public class LazySupplier<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private Holder<T> holder;

    public LazySupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        Holder<T> holder = this.holder;
        if (holder == null) {
            synchronized (this) {
                holder = this.holder;
                if (holder == null) {
                    holder = new Holder<>(supplier.get());
                    this.holder = holder;
                }
            }
        }
        return holder.value;
    }

    private static final class Holder<T> {

        private final T value;

        private Holder(T value) {
            this.value = value;
        }
    }

}
