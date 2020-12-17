/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2020
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package com.github._1c_syntax.bsl.languageserver.context.callee;

import com.github._1c_syntax.bsl.languageserver.context.DocumentContext;
import com.github._1c_syntax.bsl.languageserver.context.symbol.MethodSymbol;
import com.github._1c_syntax.bsl.languageserver.util.TestUtils;
import com.github._1c_syntax.bsl.languageserver.utils.MdoRefBuilder;
import com.github._1c_syntax.bsl.languageserver.utils.Ranges;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CalleeStorageFillerTest {

  @Autowired
  private CalleeStorageFiller calleeStorageFiller;
  @Autowired
  private CalleeStorage calleeStorage;

  @Test
  void testFindCalledMethod() {
    // given
    DocumentContext documentContext = TestUtils.getDocumentContextFromFile("./src/test/resources/context/computer/CalleeStorageFillerTest.bsl");
    calleeStorageFiller.fill(documentContext);

    // when
    Optional<Pair<MethodSymbol, Range>> calledMethodSymbol = calleeStorage.getCalledMethodSymbol(documentContext.getUri(), new Position(4, 0));

    // then
    assertThat(calledMethodSymbol).isPresent();

    assertThat(calledMethodSymbol).get()
      .extracting(Pair::getKey)
      .extracting(MethodSymbol::getName)
      .isEqualTo("Локальная");

    assertThat(calledMethodSymbol).get()
      .extracting(Pair::getValue)
      .isEqualTo(Ranges.create(4, 0, 4, 9));
  }

  @Test
  void testRebuildClearCallees() {
    // given
    DocumentContext documentContext = TestUtils.getDocumentContextFromFile("./src/test/resources/context/computer/CalleeStorageFillerTest.bsl");
    MethodSymbol methodSymbol = documentContext.getSymbolTree().getMethodSymbol("Локальная").get();

    // when
    calleeStorageFiller.fill(documentContext);
    List<Location> calleesOf = calleeStorage.getCalleesOf(MdoRefBuilder.getMdoRef(documentContext), documentContext.getModuleType(), methodSymbol);

    // then
    assertThat(calleesOf).hasSize(1);

    // when
    // recalculate
    calleeStorageFiller.fill(documentContext);
    calleesOf = calleeStorage.getCalleesOf(MdoRefBuilder.getMdoRef(documentContext), documentContext.getModuleType(), methodSymbol);

    // then
    assertThat(calleesOf).hasSize(1);
  }
}