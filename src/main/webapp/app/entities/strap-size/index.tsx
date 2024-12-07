import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StrapSize from './strap-size';
import StrapSizeDetail from './strap-size-detail';
import StrapSizeUpdate from './strap-size-update';
import StrapSizeDeleteDialog from './strap-size-delete-dialog';

const StrapSizeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StrapSize />} />
    <Route path="new" element={<StrapSizeUpdate />} />
    <Route path=":id">
      <Route index element={<StrapSizeDetail />} />
      <Route path="edit" element={<StrapSizeUpdate />} />
      <Route path="delete" element={<StrapSizeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StrapSizeRoutes;
