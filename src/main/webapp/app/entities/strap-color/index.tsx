import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StrapColor from './strap-color';
import StrapColorDetail from './strap-color-detail';
import StrapColorUpdate from './strap-color-update';
import StrapColorDeleteDialog from './strap-color-delete-dialog';

const StrapColorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StrapColor />} />
    <Route path="new" element={<StrapColorUpdate />} />
    <Route path=":id">
      <Route index element={<StrapColorDetail />} />
      <Route path="edit" element={<StrapColorUpdate />} />
      <Route path="delete" element={<StrapColorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StrapColorRoutes;
