/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.core.parsing.antlr.extractor.statement.handler;

import com.google.common.base.Optional;
import io.shardingsphere.core.parsing.antlr.extractor.statement.util.ASTUtils;
import io.shardingsphere.core.parsing.parser.sql.SQLStatement;
import io.shardingsphere.core.parsing.parser.sql.ddl.create.table.CreateTableStatement;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Create table primary key extract handler.
 * 
 * @author duhongjun
 */
public final class CreatePrimaryKeyExtractHandler implements ASTExtractHandler {
    
    @Override
    public void extract(final ParserRuleContext ancestorNode, final SQLStatement statement) {
        CreateTableStatement createTableStatement = (CreateTableStatement) statement;
        Optional<ParserRuleContext> primaryKeyNode = ASTUtils.findFirstChildNode(ancestorNode, RuleName.PRIMARY_KEY);
        if (!primaryKeyNode.isPresent()) {
            return;
        }
        Optional<ParserRuleContext> columnListNode = ASTUtils.findFirstChildNode(primaryKeyNode.get().getParent().getParent(), RuleName.COLUMN_LIST);
        if (!columnListNode.isPresent()) {
            return;
        }
        for (ParserRuleContext each : ASTUtils.getAllDescendantNodes(columnListNode.get(), RuleName.COLUMN_NAME)) {
            if (!createTableStatement.getPrimaryKeyColumns().contains(each.getText())) {
                createTableStatement.getPrimaryKeyColumns().add(each.getText());
            }
        }
    }
}