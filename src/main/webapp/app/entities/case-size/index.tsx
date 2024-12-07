import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CaseSize from './case-size';
import CaseSizeDetail from './case-size-detail';
import CaseSizeUpdate from './case-size-update';
import CaseSizeDeleteDialog from './case-size-delete-dialog';

const CaseSizeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CaseSize />} />
    <Route path="new" element={<CaseSizeUpdate />} />
    <Route path=":id">
      <Route index element={<CaseSizeDetail />} />
      <Route path="edit" element={<CaseSizeUpdate />} />
      <Route path="delete" element={<CaseSizeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CaseSizeRoutes;
